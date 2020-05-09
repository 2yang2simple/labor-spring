package com.labor.common.pagination;


import org.apache.logging.log4j.LogManager;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.Logger;
/***
 * 
 * @author yang yang1
 *
 */
//public class PaginationTag extends TagSupport {
	
public class PaginationTag  {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2986594011655631764L;
	private String action;
    private String method;
	private Logger log = LogManager.getLogger(PaginationTag.class);
    public int doEndTag() {
    	try {
//    		HttpServletRequest request =
//    			(HttpServletRequest)pageContext.getRequest();
//    		String realpath = 
//    			pageContext.getServletContext().getRealPath(request.getServletPath());
//    		String servletPath = request.getServletPath();
//    		String contextPath = request.getContextPath();
//    		
//    		String cp = String.valueOf(request.getAttribute("currentPageNumber"));
//    		if (cp==null||"".equals(cp)||"null".equals(cp)){
//    			cp = "1";
//    		}
//   
//    		StringBuffer output = new StringBuffer();
//    		output.append("<br></br><div align='right'>");
//    		
//    		//the hidden current page number value
//    		output.append("<input type='hidden' name='currentPageNumber' value='");
//    		output.append(cp);
//    		output.append("' id='");
//			output.append(action);
//			output.append("_currentPageNumber'/>");
//			//the hidden page value
//			output.append("<input type='hidden' name='page' value=''/>");
//			//the previous page button
//			output.append("<input type='button' onclick='javascript:topage(\"previous\");' value='&lt;&lt;'/>");
//			//display current page number	
//    		output.append(" | "+cp+" | ");
//			//the next page button
//    		output.append("<input type='button' onclick='javascript:topage(\"next\");' value='&gt;&gt;'/>");
//			output.append("&nbsp;&nbsp;");
//			//the page number text field
//			output.append("<input type='text' size='3' name='pageNumber' value='");
//    		output.append(cp);
//    		output.append("' id='");
//    		output.append(action);
//    		output.append("_pageNumber'/>");
//    		//the go button
//    		output.append("<input type='button' onclick='javascript:topage(\"goto\");' value='GO'/>");
//			
//    		//java script
//			output.append("<script language='Javascript'>");
//			//to page function
//			output.append("function topage(page){");
//			output.append("document.");
//			output.append(action);
//			output.append(".page.value=page;");
//			output.append("document.");
//			output.append(action);
//			output.append(".action='");
//			output.append(action);
//			output.append("!");
//			output.append(method);
//			output.append(".action';");
//			output.append("document.");
//			output.append(action);
//			output.append(".submit();");
//			output.append("}");
//			output.append("</script>");
//
//    		output.append("</div><br></br>");
//    		
//    		pageContext.getOut().println(output.toString());
    		
    		
		} catch (Exception ignored) { 
			log.error(ignored);
		}
//		return EVAL_PAGE;
    	return 1;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	

//backup
	
//	public int doEndTag() {
//    	try {
//    		HttpServletRequest request =
//    			(HttpServletRequest)pageContext.getRequest();
//    		String realpath = 
//    			pageContext.getServletContext().getRealPath(request.getServletPath());
//    		String servletPath = request.getServletPath();
//    		String contextPath = request.getContextPath();
//    		
//    		String cp = (String)request.getAttribute("currentPageNumber");
//    		if (cp==null||"".equals(cp)||"null".equals(cp)){
//    			cp = "1";
//    		}
//   
//    		StringBuffer output = new StringBuffer();
//    		output.append("<br></br><div align='right'>");
//    		output.append("<form id='");
//    		output.append(action);
//			output.append("' name='");
//			output.append(action);
//			output.append("' action='");
//    		output.append(contextPath+"/"+action);
//    		output.append(".action' method='post'>");
//    		output.append("<table border='0' class='wwFormTable'><tr><div align='right'>");
//    		
//    		output.append("<td class='tdLabel'></td>");
//    		
//    		output.append("<td>");
//    		
//    		output.append("&nbsp<a href='");
//    		output.append(contextPath+"/"+action);
//    		output.append("!");
//    		output.append(method);
//    		output.append(".action?page=previous&currentPageNumber=");
//    		output.append(cp);
//    		//previous
//    		output.append("'><<</a>&nbsp");
//    		
//    		output.append("</td><td>");
//    		output.append(" | "+cp+" | ");
//    		output.append("</td><td>");
//    		
//    		output.append("&nbsp<a href='");
//    		output.append(contextPath+"/"+action);
//    		output.append("!");
//    		output.append(method);
//    		output.append(".action?page=next&currentPageNumber=");
//    		output.append(cp);
//    		//next
//    		output.append("'>>></a>&nbsp");
//    		
//    		output.append("</td><td>");
//    		
//    		output.append("<input type='text' size='3' name='pageNumber' value='");
//    		output.append(cp);
//    		output.append("' id='");
//    		output.append(action);
//    		output.append("_pageNumber'/>");
//    		
//			
//			output.append("</td><td>");
//			output.append("<input type='submit' id='");
//			output.append(action);
//			output.append("__");
//			output.append(method);
//			output.append("' name='method:");
//			output.append(method);
//			output.append("' value='Go'/>");
//			output.append("</td></div></tr></table></form>");
//
//    		output.append("</div><br></br>");
//    		
//    		pageContext.getOut().println(output.toString());
//    		
//    		
//		} catch (Exception ignored) { 
//			log.error(ignored);
//		}
//		return EVAL_PAGE;
//	}
}
