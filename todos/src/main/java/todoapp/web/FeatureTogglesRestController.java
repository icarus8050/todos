package todoapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import todoapp.web.model.FeatureTogglesProperties;

@RestController
@RequestMapping("/api")
public class FeatureTogglesRestController {
	
	private final Logger log = LoggerFactory.getLogger(FeatureTogglesRestController.class);
	
	private FeatureTogglesProperties featureTogglesProperties;
	
	public FeatureTogglesRestController(FeatureTogglesProperties featureTogglesProperties) {
		this.featureTogglesProperties = featureTogglesProperties;
	}


	@GetMapping("/feature-toggles")
	public FeatureTogglesProperties getFeatureToggle() {
		return featureTogglesProperties;
	}
}
