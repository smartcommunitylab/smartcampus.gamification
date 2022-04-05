package eu.trentorise.game.test_utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.command.Command;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatelessKnowledgeSession;

public final class Utils {

	// static class, no needs to instantiate
	private Utils() {
	}

	public static Date date(String isoDate) {
		return LocalDateTime.parse(isoDate).toDate();
	}

	public static void main(String args[]) throws FileNotFoundException {
		System.out.println("simple test drool");
//		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//		kbuilder.add(ResourceFactory.newClassPathResource("rules/testLevel/simple.drl", Utils.class), ResourceType.DRL);
//		if (kbuilder.hasErrors()) {
//			System.err.println(kbuilder.getErrors().toString());
//		}
//		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
//		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
//		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
//		Applicant applicant1 = new Applicant("Mr John Smith", 17);
//		Applicant applicant2 = new Applicant("Mr John Smith", 16);
//		System.out.println("applicant1->" + applicant1.isValid() + " applicant2->" + applicant2.isValid());
//		ksession.execute(Arrays.asList( new Object[] { applicant1,  applicant2 }) );
//		System.out.println("applicant1->" + applicant1.isValid() + " applicant2->" + applicant2.isValid());

		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kfs = kieServices.newKieFileSystem();
		kfs.write(ResourceFactory.newClassPathResource("rules/testLevel/simple.drl", Utils.class));
		KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
		Results results = kieBuilder.getResults();
		if (results.hasMessages(Message.Level.ERROR)) {
			System.out.println(results.getMessages());
			throw new IllegalStateException("### errors ###");
		}
		KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
		StatelessKieSession kieSession = kieContainer.newStatelessKieSession();
		Applicant applicant1 = new Applicant("Mr John Smith", 17);
		Applicant applicant2 = new Applicant("Mr Khurshid Nawaz", 16);
		Application application = new Application();
		application.setDateApplied(new Date());
		System.out.println("applicant(" + applicant1.getName() + ")->" + applicant1.isValid() + " applicant(" + applicant2.getName() + ")->" + applicant2.isValid() + " application ->" + application.isValid());
        List<Command> cmds = new ArrayList<Command>();
        cmds.add(CommandFactory.newInsertElements(Arrays.asList( new Object[] { applicant1,  applicant2, application })));
		kieSession.execute(CommandFactory.newBatchExecution(cmds));
		System.out.println("applicant(" + applicant1.getName() + ")->" + applicant1.isValid() + " applicant(" + applicant2.getName() + ")->" + applicant2.isValid() + " application ->" + application.isValid());
    }
}
