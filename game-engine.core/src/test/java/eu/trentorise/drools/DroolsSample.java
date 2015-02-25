package eu.trentorise.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.KieResources;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

public class DroolsSample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KieServices kieServices = KieServices.Factory.get();

		KieResources res = kieServices.getResources();

		org.kie.api.io.Resource r1 = res
				.newFileSystemResource("src/test/resources/action1/rule1.drl");
		KieFileSystem kfs = kieServices.newKieFileSystem();
		kfs.write(r1);

		KieBuilder kContainer = kieServices.newKieBuilder(kfs).buildAll();

		KieContainer kieContainer = kieServices.newKieContainer(kieServices
				.getRepository().getDefaultReleaseId());

		StatelessKieSession kSession = kieContainer.newStatelessKieSession();
		SampleModel sample = new SampleModel("yop");
		kSession.execute(sample);
		System.out.println(sample.isValid());

	}
}
