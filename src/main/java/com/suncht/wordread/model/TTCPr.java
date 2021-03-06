package com.suncht.wordread.model;

import java.io.Serializable;
import java.math.BigInteger;

import com.suncht.wordread.parser.mapping.IWordTableMemoryMappingVisitor;

/**
 * word的单元格
 * @author changtan.sun
 *
 */
public class TTCPr implements Serializable, Cloneable {
	private static final long serialVersionUID = 463466191913957614L;
	/**
	 * 单元格的类型
	 */
	private TTCPrEnum type;
	/**
	 * 在word中实际行号
	 */
	private int realRowIndex;
	/**
	 * 在word中实际列号
	 */
	private int realColumnIndex;
	
	/**
	 * 逻辑行号
	 */
	private int logicRowIndex;
	
	/**
	 * 逻辑列号
	 */
	private int logicColumnIndex;
	/**
	 * 根单元格（如果是被合并的单元格(包括行合并、列合并)， 则指向合并开始的单元格）
	 *  * 例子：
	 *	|-------|-----------------------------	|
	 	| 任务   	|    	   	故障影响                              	|	
	 	|      	| ----------------------------	|
	 	| 阶段   	| 局部影响 | 高一层次影响 | 最终影响		|
	 	|-------|-----------------------------	|
	 	
	 	说明：“任务阶段”所在单元格进行2行合并，其中第一个单元格是root，其他单元格的Root是第一个单元格
	 */
	private TTCPr root;
	/**
	 * 单元格的数据内容
	 */
	private WordTableCellContent<?> content;
	
	/**
	 * 单元格的宽度
	 */
	private BigInteger width;
	/**
	 * 合并了多少行
	 */
	private int rowSpan = 0;
	/**
	 * 合并了多少列
	 */
	private int colSpan = 0;

	/**
	 * 父单元格，当列合并时有效
	 * 例子：
	 *	|-------|-----------------------------	|
	 	| 任务   	|    	   	故障影响                              	|	
	 	|      	| ----------------------------	|
	 	| 阶段   	| 局部影响 | 高一层次影响 | 最终影响		|
	 	|-------|-----------------------------	|
	 	说明：“局部影响”所在单元格的父单元格parent是“故障影响”所在单元格
	 */
	//private TTCPr parent;

	/**
	 * 是否有效单元格。 被合并的单元格不属于有效单元格
	 * @return
	 */
	public boolean isValid() {
		return type == TTCPrEnum.VM_S || type == TTCPrEnum.NONE || type == TTCPrEnum.HM_S || type == TTCPrEnum.HVM_S;
	}

	/**
	 * 是否进行了列合并
	 * @return
	 */
	public boolean isDoneColSpan() {
		return type == TTCPrEnum.HM_S || type == TTCPrEnum.HM || type == TTCPrEnum.HVM_S;
	}

	/**
	 * 是否进行了行合并
	 * @return
	 */
	public boolean isDoneRowSpan() {
		return type == TTCPrEnum.VM_S || type == TTCPrEnum.VM || type == TTCPrEnum.HVM_S;
	}

	/**
	 * 单元格坐标
	 * @return
	 */
	public String getCellPosition() {
		return realRowIndex + "-" + realColumnIndex;
	}

	public TTCPrEnum getType() {
		return type;
	}

	public void setType(TTCPrEnum type) {
		this.type = type;
	}

	public int getRealRowIndex() {
		return realRowIndex;
	}

	public void setRealRowIndex(int realRowIndex) {
		this.realRowIndex = realRowIndex;
	}

	public int getRealColumnIndex() {
		return realColumnIndex;
	}

	public void setRealColumnIndex(int realColumnIndex) {
		this.realColumnIndex = realColumnIndex;
	}
	
	public int getLogicRowIndex() {
		return logicRowIndex;
	}

	public void setLogicRowIndex(int logicRowIndex) {
		this.logicRowIndex = logicRowIndex;
	}

	public int getLogicColumnIndex() {
		return logicColumnIndex;
	}

	public void setLogicColumnIndex(int logicColumnIndex) {
		this.logicColumnIndex = logicColumnIndex;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public WordTableCellContent<?> getContent() {
		if (root != null) {
			return root.getContent();
		}
		return content;
	}

	public void setContent(WordTableCellContent<?> content) {
		this.content = content;
	}

	public TTCPr getRoot() {
		return root;
	}

	public void setRoot(TTCPr root) {
		this.root = root;
	}

//	public TTCPr getParent() {
//		return parent;
//	}
//
//	public void setParent(TTCPr parent) {
//		this.parent = parent;
//	}

	public BigInteger getWidth() {
		return width;
	}

	public void setWidth(BigInteger width) {
		this.width = width;
	}

	@Override
	public String toString() {
		if (root != null) {
			return "TTCPr [root=" + root + "]";
		}
		return "TTCPr [content=" + content.getData() + "]";
	}

	public void accept(IWordTableMemoryMappingVisitor visitor, int realRowIndex, int realColumnIndex) {
		visitor.visit(this, realRowIndex, realColumnIndex);
	}

	public static enum TTCPrEnum {
		/**
		 * 无任何格式
		 */
		NONE,
		/**
		 * 行合并的开始
		 */
		VM_S,
		/**
		 * 被行合并
		 */
		VM,
		/**
		 * 列合并的开始
		 */
		HM_S,
		/**
		 * 被列合并
		 */
		HM,
		/**
		 * 行合并的开始，又是列合并的开始
		 */
		HVM_S
	}
}
