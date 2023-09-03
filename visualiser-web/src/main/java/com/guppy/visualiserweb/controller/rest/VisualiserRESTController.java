package com.guppy.visualiserweb.controller.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guppy.visualiserweb.common.Util;
import com.guppy.visualiserweb.data.model.DropdownItem;

@RestController
@RequestMapping("/api")
public class VisualiserRESTController {

	@GetMapping("/broadcasting_impl")
    public List<DropdownItem> getDropdownData() {
		return Util.getRegisteredBroadcastingStrategies();
    }


}
