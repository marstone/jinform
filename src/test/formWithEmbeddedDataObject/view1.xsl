<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:my="http://schemas.microsoft.com/office/infopath/2003/myXSD/2005-05-20T06:57:42" xmlns:xd="http://schemas.microsoft.com/office/infopath/2003" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:xdExtension="http://schemas.microsoft.com/office/infopath/2003/xslt/extension" xmlns:xdXDocument="http://schemas.microsoft.com/office/infopath/2003/xslt/xDocument" xmlns:xdSolution="http://schemas.microsoft.com/office/infopath/2003/xslt/solution" xmlns:xdFormatting="http://schemas.microsoft.com/office/infopath/2003/xslt/formatting" xmlns:xdImage="http://schemas.microsoft.com/office/infopath/2003/xslt/xImage" xmlns:xdUtil="http://schemas.microsoft.com/office/infopath/2003/xslt/Util" xmlns:xdMath="http://schemas.microsoft.com/office/infopath/2003/xslt/Math" xmlns:xdDate="http://schemas.microsoft.com/office/infopath/2003/xslt/Date" xmlns:sig="http://www.w3.org/2000/09/xmldsig#" xmlns:xdSignatureProperties="http://schemas.microsoft.com/office/infopath/2003/SignatureProperties">
	<xsl:output method="html" indent="no"/>
	<xsl:template match="my:applicationDataV2">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html"></meta>
				<style controlStyle="controlStyle">@media screen 			{ 			BODY{margin-left:21px;background-position:21px 0px;} 			} 		BODY{color:windowtext;background-color:window;layout-grid:none;} 		.xdListItem {display:inline-block;width:100%;vertical-align:text-top;} 		.xdListBox,.xdComboBox{margin:1px;} 		.xdInlinePicture{margin:1px; BEHAVIOR: url(#default#urn::xdPicture) } 		.xdLinkedPicture{margin:1px; BEHAVIOR: url(#default#urn::xdPicture) url(#default#urn::controls/Binder) } 		.xdSection{border:1pt solid #FFFFFF;margin:6px 0px 6px 0px;padding:1px 1px 1px 5px;} 		.xdRepeatingSection{border:1pt solid #FFFFFF;margin:6px 0px 6px 0px;padding:1px 1px 1px 5px;} 		.xdBehavior_Formatting {BEHAVIOR: url(#default#urn::controls/Binder) url(#default#Formatting);} 	 .xdBehavior_FormattingNoBUI{BEHAVIOR: url(#default#CalPopup) url(#default#urn::controls/Binder) url(#default#Formatting);} 	.xdExpressionBox{margin: 1px;padding:1px;word-wrap: break-word;text-overflow: ellipsis;overflow-x:hidden;}.xdBehavior_GhostedText,.xdBehavior_GhostedTextNoBUI{BEHAVIOR: url(#default#urn::controls/Binder) url(#default#TextField) url(#default#GhostedText);}	.xdBehavior_GTFormatting{BEHAVIOR: url(#default#urn::controls/Binder) url(#default#Formatting) url(#default#GhostedText);}	.xdBehavior_GTFormattingNoBUI{BEHAVIOR: url(#default#CalPopup) url(#default#urn::controls/Binder) url(#default#Formatting) url(#default#GhostedText);}	.xdBehavior_Boolean{BEHAVIOR: url(#default#urn::controls/Binder) url(#default#BooleanHelper);}	.xdBehavior_Select{BEHAVIOR: url(#default#urn::controls/Binder) url(#default#SelectHelper);}	.xdRepeatingTable{BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none; BORDER-COLLAPSE: collapse; WORD-WRAP: break-word;}.xdScrollableRegion{BEHAVIOR: url(#default#ScrollableRegion);} 		.xdMaster{BEHAVIOR: url(#default#MasterHelper);} 		.xdActiveX{margin:1px; BEHAVIOR: url(#default#ActiveX);} 		.xdFileAttachment{display:inline-block;margin:1px;BEHAVIOR:url(#default#urn::xdFileAttachment);} 		.xdPageBreak{display: none;}BODY{margin-right:21px;} 		.xdTextBoxRTL{display:inline-block;white-space:nowrap;text-overflow:ellipsis;;padding:1px;margin:1px;border: 1pt solid #dcdcdc;color:windowtext;background-color:window;overflow:hidden;text-align:right;} 		.xdRichTextBoxRTL{display:inline-block;;padding:1px;margin:1px;border: 1pt solid #dcdcdc;color:windowtext;background-color:window;overflow-x:hidden;word-wrap:break-word;text-overflow:ellipsis;text-align:right;font-weight:normal;font-style:normal;text-decoration:none;vertical-align:baseline;} 		.xdDTTextRTL{height:100%;width:100%;margin-left:22px;overflow:hidden;padding:0px;white-space:nowrap;} 		.xdDTButtonRTL{margin-right:-21px;height:18px;width:20px;behavior: url(#default#DTPicker);}.xdTextBox{display:inline-block;white-space:nowrap;text-overflow:ellipsis;;padding:1px;margin:1px;border: 1pt solid #dcdcdc;color:windowtext;background-color:window;overflow:hidden;text-align:left;} 		.xdRichTextBox{display:inline-block;;padding:1px;margin:1px;border: 1pt solid #dcdcdc;color:windowtext;background-color:window;overflow-x:hidden;word-wrap:break-word;text-overflow:ellipsis;text-align:left;font-weight:normal;font-style:normal;text-decoration:none;vertical-align:baseline;} 		.xdDTPicker{;display:inline;margin:1px;margin-bottom: 2px;border: 1pt solid #dcdcdc;color:windowtext;background-color:window;overflow:hidden;} 		.xdDTText{height:100%;width:100%;margin-right:22px;overflow:hidden;padding:0px;white-space:nowrap;} 		.xdDTButton{margin-left:-21px;height:18px;width:20px;behavior: url(#default#DTPicker);} 		.xdRepeatingTable TD {VERTICAL-ALIGN: top;}</style>
				<style tableEditor="TableStyleRulesID">TABLE.xdLayout TD {
	BORDER-RIGHT: medium none; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: medium none
}
TABLE.msoUcTable TD {
	BORDER-RIGHT: 1pt solid; BORDER-TOP: 1pt solid; BORDER-LEFT: 1pt solid; BORDER-BOTTOM: 1pt solid
}
TABLE {
	BEHAVIOR: url (#default#urn::tables/NDTable)
}
</style>
				<style languageStyle="languageStyle">BODY {
	FONT-SIZE: 10pt; FONT-FAMILY: Verdana
}
TABLE {
	FONT-SIZE: 10pt; FONT-FAMILY: Verdana
}
SELECT {
	FONT-SIZE: 10pt; FONT-FAMILY: Verdana
}
.optionalPlaceholder {
	PADDING-LEFT: 20px; FONT-WEIGHT: normal; FONT-SIZE: xx-small; BEHAVIOR: url(#default#xOptional); COLOR: #333333; FONT-STYLE: normal; FONT-FAMILY: Verdana; TEXT-DECORATION: none
}
.langFont {
	FONT-FAMILY: Verdana
}
</style>
			</head>
			<body>
				<div><xsl:apply-templates select="my:identification" mode="_1"/>
				</div>
				<div> </div>
				<div><xsl:apply-templates select="my:previousJobs" mode="_2"/>
				</div>
				<div> </div>
				<div align="left"> </div>
				<div>
					<input class="langFont" title="" style="BEHAVIOR: url(#default#ActionButton)" type="button" value="Submit" xd:xctname="Button" xd:CtrlId="CTRL15_5" xd:action="submit" tabIndex="0"/>
				</div>
				<div> </div>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="my:identification" mode="_1">
		<div class="xdSection xdRepeating" title="" style="MARGIN-BOTTOM: 6px; WIDTH: 653px; HEIGHT: 146px" align="left" xd:xctname="Section" xd:CtrlId="CTRL1" tabIndex="-1">
			<div>
				<table class="xdFormLayout xdLayout" style="TABLE-LAYOUT: fixed; WIDTH: 633px; BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-COLLAPSE: collapse; WORD-WRAP: break-word; BORDER-BOTTOM-STYLE: none" border="1">
					<colgroup>
						<col style="WIDTH: 266px"></col>
						<col style="WIDTH: 367px"></col>
					</colgroup>
					<tbody vAlign="top">
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>First Name:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div><span class="xdTextBox" hideFocus="1" title="" tabIndex="0" xd:xctname="PlainText" xd:CtrlId="CTRL2" xd:binding="my:firstName" style="WIDTH: 100%">
										<xsl:value-of select="my:firstName"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>Last Name:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div><span class="xdTextBox" hideFocus="1" title="" tabIndex="0" xd:xctname="PlainText" xd:CtrlId="CTRL3" xd:binding="my:lastName" style="WIDTH: 100%">
										<xsl:value-of select="my:lastName"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>Birth Date:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>
									<div class="xdDTPicker" title="" style="WIDTH: 100%" noWrap="1" xd:xctname="DTPicker" xd:CtrlId="CTRL4"><span class="xdDTText xdBehavior_FormattingNoBUI" hideFocus="1" contentEditable="true" tabIndex="0" xd:xctname="DTPicker_DTText" xd:binding="my:birthDate" xd:datafmt="&quot;date&quot;,&quot;dateFormat:Short Date;&quot;" xd:boundProp="xd:num" xd:innerCtrl="_DTText">
											<xsl:attribute name="xd:num">
												<xsl:value-of select="my:birthDate"/>
											</xsl:attribute>
											<xsl:choose>
												<xsl:when test="function-available('xdFormatting:formatString')">
													<xsl:value-of select="xdFormatting:formatString(my:birthDate,&quot;date&quot;,&quot;dateFormat:Short Date;&quot;)"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="my:birthDate"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
										<button class="xdDTButton" xd:xctname="DTPicker_DTButton" xd:innerCtrl="_DTButton" tabIndex="-1">
											<img src="res://infopath.exe/calendar.gif" Linked="true"/>
										</button>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>Sex:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>
									<div><input class="xdBehavior_Boolean" title="" type="radio" name="{generate-id(my:sex)}" tabIndex="0" xd:xctname="OptionButton" xd:CtrlId="CTRL5" xd:binding="my:sex" xd:boundProp="xd:value" xd:onValue="1">
											<xsl:attribute name="xd:value">
												<xsl:value-of select="my:sex"/>
											</xsl:attribute>
											<xsl:if test="my:sex=&quot;1&quot;">
												<xsl:attribute name="CHECKED">CHECKED</xsl:attribute>
											</xsl:if>
										</input> Male</div>
									<div><input class="xdBehavior_Boolean" title="" type="radio" name="{generate-id(my:sex)}" tabIndex="0" xd:xctname="OptionButton" xd:CtrlId="CTRL6" xd:binding="my:sex" xd:boundProp="xd:value" xd:onValue="2">
											<xsl:attribute name="xd:value">
												<xsl:value-of select="my:sex"/>
											</xsl:attribute>
											<xsl:if test="my:sex=&quot;2&quot;">
												<xsl:attribute name="CHECKED">CHECKED</xsl:attribute>
											</xsl:if>
										</input> Female</div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div> </div>
		</div>
	</xsl:template>
	<xsl:template match="my:previousJobs" mode="_2">
		<div class="xdRepeatingSection xdRepeating" title="" style="MARGIN-BOTTOM: 6px; WIDTH: 651px; HEIGHT: 97px" align="left" xd:xctname="RepeatingSection" xd:CtrlId="CTRL7" tabIndex="-1">
			<div>
				<table class="xdFormLayout xdLayout" style="TABLE-LAYOUT: fixed; WIDTH: 631px; BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-COLLAPSE: collapse; WORD-WRAP: break-word; BORDER-BOTTOM-STYLE: none" border="1">
					<colgroup>
						<col style="WIDTH: 266px"></col>
						<col style="WIDTH: 365px"></col>
					</colgroup>
					<tbody vAlign="top">
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>Employer:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div><span class="xdTextBox" hideFocus="1" title="" tabIndex="0" xd:xctname="PlainText" xd:CtrlId="CTRL8" xd:binding="my:employer" style="WIDTH: 100%">
										<xsl:value-of select="my:employer"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>Job:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>
									<select class="xdComboBox xdBehavior_Select" title="" style="WIDTH: 100%" size="1" xd:xctname="DropDown" xd:CtrlId="CTRL10" xd:binding="my:job" xd:boundProp="value" value="" tabIndex="0">
										<xsl:attribute name="value">
											<xsl:value-of select="my:job"/>
										</xsl:attribute>
										<xsl:choose>
											<xsl:when test="function-available('xdXDocument:GetDOM')">
												<option/>
												<xsl:variable name="val" select="my:job"/>
												<xsl:if test="not(xdXDocument:GetDOM(&quot;jobs&quot;)/jobs/job[@code=$val] or $val='')">
													<option selected="selected">
														<xsl:attribute name="value">
															<xsl:value-of select="$val"/>
														</xsl:attribute>
														<xsl:value-of select="$val"/>
													</option>
												</xsl:if>
												<xsl:for-each select="xdXDocument:GetDOM(&quot;jobs&quot;)/jobs/job">
													<option>
														<xsl:attribute name="value">
															<xsl:value-of select="@code"/>
														</xsl:attribute>
														<xsl:if test="$val=@code">
															<xsl:attribute name="selected">selected</xsl:attribute>
														</xsl:if>
														<xsl:value-of select="."/>
													</option>
												</xsl:for-each>
											</xsl:when>
											<xsl:otherwise>
												<option>
													<xsl:value-of select="my:job"/>
												</option>
											</xsl:otherwise>
										</xsl:choose>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>From:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>
									<div class="xdDTPicker" title="" style="WIDTH: 100%" noWrap="1" xd:xctname="DTPicker" xd:CtrlId="CTRL11"><span class="xdDTText xdBehavior_FormattingNoBUI" hideFocus="1" contentEditable="true" tabIndex="0" xd:xctname="DTPicker_DTText" xd:binding="my:jobFrom" xd:datafmt="&quot;date&quot;,&quot;dateFormat:Short Date;&quot;" xd:boundProp="xd:num" xd:innerCtrl="_DTText">
											<xsl:attribute name="xd:num">
												<xsl:value-of select="my:jobFrom"/>
											</xsl:attribute>
											<xsl:choose>
												<xsl:when test="function-available('xdFormatting:formatString')">
													<xsl:value-of select="xdFormatting:formatString(my:jobFrom,&quot;date&quot;,&quot;dateFormat:Short Date;&quot;)"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="my:jobFrom"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
										<button class="xdDTButton" xd:xctname="DTPicker_DTButton" xd:innerCtrl="_DTButton" tabIndex="-1">
											<img src="res://infopath.exe/calendar.gif"/>
										</button>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>To:</div>
							</td>
							<td style="BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none">
								<div>
									<div class="xdDTPicker" title="" style="WIDTH: 100%" noWrap="1" xd:xctname="DTPicker" xd:CtrlId="CTRL12"><span class="xdDTText xdBehavior_FormattingNoBUI" hideFocus="1" contentEditable="true" tabIndex="0" xd:xctname="DTPicker_DTText" xd:binding="my:jobTo" xd:datafmt="&quot;date&quot;,&quot;dateFormat:Short Date;&quot;" xd:boundProp="xd:num" xd:innerCtrl="_DTText">
											<xsl:attribute name="xd:num">
												<xsl:value-of select="my:jobTo"/>
											</xsl:attribute>
											<xsl:choose>
												<xsl:when test="function-available('xdFormatting:formatString')">
													<xsl:value-of select="xdFormatting:formatString(my:jobTo,&quot;date&quot;,&quot;dateFormat:Short Date;&quot;)"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="my:jobTo"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
										<button class="xdDTButton" xd:xctname="DTPicker_DTButton" xd:innerCtrl="_DTButton" tabIndex="-1">
											<img src="res://infopath.exe/calendar.gif"/>
										</button>
									</div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>
