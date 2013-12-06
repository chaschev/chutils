/**
 * Copyright (C) 2011 K Venkata Sudhakar <kvenkatasudhakar@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bethecoder.table;

import com.bethecoder.table.spec.AsciiTable;

/**
 * Represents ASCII table header.
 * 
 * @author K Venkata Sudhakar (kvenkatasudhakar@gmail.com)
 * @version 1.0
 *
 */
public class ASCIITableHeader {

	private String headerName;
	private int headerAlign = AsciiTable.DEFAULT_HEADER_ALIGN;
	private int dataAlign = AsciiTable.DEFAULT_DATA_ALIGN;
    private int maxWidth;

	public ASCIITableHeader(String headerName) {
		this.headerName = headerName;
	}

	public ASCIITableHeader(String headerName, int dataAlign) {
		this.headerName = headerName;
		this.dataAlign = dataAlign;
	}

	public ASCIITableHeader(String headerName, int dataAlign, int headerAlign) {
		this.headerName = headerName;
		this.dataAlign = dataAlign;
		this.headerAlign = headerAlign;
	}

	public String getHeaderName() {
		return headerName;
	}
	
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	public int getHeaderAlign() {
		return headerAlign;
	}
	
	public void setHeaderAlign(int headerAlign) {
		this.headerAlign = headerAlign;
	}
	
	public int getDataAlign() {
		return dataAlign;
	}
	
	public void setDataAlign(int dataAlign) {
		this.dataAlign = dataAlign;
	}

    public ASCIITableHeader maxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public int maxWidth() {
        return maxWidth;
    }

    public static ASCIITableHeader h(String headerName){
        return new ASCIITableHeader(headerName, AsciiTable.ALIGN_LEFT);
    }

    public static ASCIITableHeader h(String headerName, int dataAlign){
        return new ASCIITableHeader(headerName, dataAlign);
    }

    public static ASCIITableHeader h(String headerName, int dataAlign, int headerAlign){
        return new ASCIITableHeader(headerName, dataAlign, headerAlign);
    }
}
