/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dictionarysubscribing.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dictionarysubscribing.api.DictionarySubscribingService;
import org.openmrs.module.metadatasharing.ImportedPackage;
import org.openmrs.module.metadatasharing.subscription.SubscriptionHeader;
import org.openmrs.module.metadatasharing.updater.SubscriptionUpdater;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class DictionarySubscribingController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/dictionarysubscribing/subscribe", method = RequestMethod.GET)
	public void subscribe(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
		model.addAttribute("conceptCount", Context.getConceptService().getAllConcepts().size());
	}
	
	@RequestMapping("/module/dictionarysubscribing/subscribed")
	public String subscribed(@RequestParam(required = false) String url, HttpSession httpSession, ModelMap model) {
		
		DictionarySubscribingService dss = Context.getService(DictionarySubscribingService.class);
		
		dss.subscribeToDictionary(url);
		
		ImportedPackage importedPackage = dss.getSubscribedDictionary();
		if(importedPackage.hasSubscriptionErrors()){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Unable to subscribe to url: "+importedPackage.getSubscriptionStatus());
		}
		
		model.addAttribute("dictionary", dss.getSubscribedDictionary());
		model.addAttribute("url", url);
		
		return null;
	}
}