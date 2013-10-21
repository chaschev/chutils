package chaschev.lang;

/**
 * User: chaschev
 * Date: 9/25/12
 */
public class OpenStringBuilder implements Appendable, CharSequence {
    protected char[] buf;
    protected int len;

    public OpenStringBuilder() {
        this(32);
    }

    public OpenStringBuilder(int size) {
        buf = new char[size];
    }

    public OpenStringBuilder(char[] arr, int len) {
        set(arr, len);
    }

    public void setLength(int len) {
        this.len = len;
    }

    public void set(char[] arr, int end) {
        this.buf = arr;
        this.len = end;
    }

    public char[] getArray() {
        return buf;
    }

    public int size() {
        return len;
    }

    public int length() {
        return len;
    }

    public int capacity() {
        return buf.length;
    }

    public OpenStringBuilder append(CharSequence csq) {
        append(csq, 0, csq.length());
        return this;
    }

    public OpenStringBuilder append(CharSequence csq, int start, int end) {
        reserve(end - start);
        for (int i = start; i < end; i++) {
            unsafeWrite(csq.charAt(i));
        }
        return this;
    }

    public OpenStringBuilder append(char c) {
        write(c);
        return this;
    }

    public char charAt(int index) {
        return buf[index];
    }

    public void setCharAt(int index, char ch) {
        buf[index] = ch;
    }

    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException(); // todo
    }

    public void unsafeWrite(char b) {
        buf[len++] = b;
    }

    public void unsafeWrite(int b) {
        unsafeWrite((char) b);
    }

    public void unsafeWrite(char b[], int off, int len) {
        System.arraycopy(b, off, buf, this.len, len);
        this.len += len;
    }

    protected void resize(int len) {
        char newbuf[] = new char[Math.max(buf.length << 1, len)];
        System.arraycopy(buf, 0, newbuf, 0, size());
        buf = newbuf;
    }

    public void reserve(int num) {
        if (len + num > buf.length) {
            if (num > 10) {
                resize(len * 3 / 2 + num);
            }else{
                resize(len + num);
            }
        }
    }

    public void write(char b) {
        if (len >= buf.length) {
            resize(len * 11 / 10 + 5);
        }
        unsafeWrite(b);
    }

    public void write(int b) {
        write((char) b);
    }

    public final void write(char[] b) {
        write(b, 0, b.length);
    }

    public void write(char b[], int off, int len) {
        reserve(len);
        unsafeWrite(b, off, len);
    }

    public final void write(OpenStringBuilder arr) {
        write(arr.buf, 0, len);
    }

    public void write(String s) {
        reserve(s.length());
        s.getChars(0, s.length(), buf, len);
        len += s.length();
    }

    public void flush() {
    }

    public final void reset() {
        len = 0;
    }

    public char[] toCharArray() {
        char newbuf[] = new char[size()];
        System.arraycopy(buf, 0, newbuf, 0, size());
        return newbuf;
    }

    public String toString() {
        return new String(buf, 0, size());
    }

    public OpenStringBuilder trimEnd() {
        int i = len - 1;

        for (; i >= 0 && Character.isWhitespace(buf[i]); i--) {
        }

        len = i + 1;

        return this;
    }

    public final boolean isEmpty() {
        return len == 0;
    }

    public void appendSeparator(CharSequence s) {
        if (!endsWith(s)) {
            append(s);
        }
    }

    public boolean endsWith(CharSequence s) {
        final int length2 = s.length();

        if (len < length2) return false;

        for (int i = len - length2, j = 0; j < length2; i++, j++) {
            if (buf[i] != s.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    public void append(char[] cbuf, int off, int len) {
        reserve(len);
        System.arraycopy(cbuf, off, buf, this.len, len);
        this.len += len;
    }
}
