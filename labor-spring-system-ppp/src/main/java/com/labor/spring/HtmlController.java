package com.labor.spring;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.labor.base.cfg.DDValueVO;
import com.labor.base.subject.SubjectServiceImpl;
import com.labor.base.subject.SubjectServiceIntf;
import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceTransactionProxy;
import com.labor.common.util.StringUtil;

import com.labor.spring.core.controller.AbstractCoreController;

import javassist.expr.NewArray;

@Controller
public class HtmlController extends AbstractCoreController{

	private static String CORE_PREFIX = "00.";
	private static String PROD_PREFIX = "01.ppp/01.";
	private static String PROJ_PREFIX = "01.ppp/02.";
	private static String PROF_PREFIX = "01.ppp/03.";
	
	
	//url rewrite;
	@RequestMapping(value = { "/rt/uuid/{uuid}.html" }, method = RequestMethod.GET)
	public String toRichtextViewPage(ModelMap map,@PathVariable(value="uuid") String uuid) {	
		map.addAttribute("uuid",uuid);
		return CORE_PREFIX + "core/richtext/viewer";
	}
	//url rewrite;
	@RequestMapping(value = { "/rt/name/{name}.html" }, method = RequestMethod.GET)
	public String toRichtextNamePage(ModelMap map,@PathVariable(value="name") String name) {	
		map.addAttribute("name",name);
		return CORE_PREFIX + "core/richtext/viewer";
	}

	@RequestMapping(value = { "/core/richtext/{page}" }, method = RequestMethod.GET)
	public String toRichtextPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/richtext/"+page;
	}
	@RequestMapping(value = { "/core/dictionary/{page}" }, method = RequestMethod.GET)
	public String toDictionaryPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/dictionary/"+page;
	}
		
	/***********************
	 * proof
	 */
	@RequestMapping(value = { "/prof/document/{page}" }, method = RequestMethod.GET)
	public String toProofDocumentPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return PROF_PREFIX + "prof/document/"+page;
	}
	//url rewrite;
	@RequestMapping(value = { "/prof/document/{uuid}.html" }, method = RequestMethod.GET)
	public String toProofDocumentViewPage(ModelMap map,@PathVariable(value="uuid") String uuid) {	
		map.addAttribute("uuid",uuid);
		return PROF_PREFIX + "prof/document/viewer";
	}
	@RequestMapping(value = { "/prof/gallery/{page}" }, method = RequestMethod.GET)
	public String toProofGalleryPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return PROF_PREFIX + "prof/gallery/"+page;
	}
	@RequestMapping(value = { "/prof/tag/{page}" }, method = RequestMethod.GET)
	public String toProofTagPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return PROF_PREFIX + "prof/tag/"+page;
	}
	
	/***********************
	 * product
	 */
	@RequestMapping(value = { "/prod/{page}" }, method = RequestMethod.GET)
	public String toProductPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return PROD_PREFIX + "prod/"+page;
	}
	
	/***********************
	 * project
	 */
	@RequestMapping(value = { "/proj/{page}" }, method = RequestMethod.GET)
	public String toProjectPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return PROJ_PREFIX + "proj/"+page;
	}
	
}
