/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.drools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.drools.core.io.impl.ByteArrayResource;
import org.drools.core.io.impl.ClassPathResource;
import org.drools.template.ObjectDataCompiler;
import org.drools.verifier.Verifier;
import org.drools.verifier.VerifierError;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.command.Command;
import org.kie.api.io.KieResources;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.command.CommandFactory;

public class DroolsSample {

	@Test
	public void main() {
		KieServices kieServices = KieServices.Factory.get();

		KieResources res = kieServices.getResources();

		org.kie.api.io.Resource r1 = res
				.newFileSystemResource("src/test/resources/action1/rule1.drl");
		KieFileSystem kfs = kieServices.newKieFileSystem();
		kfs.write(r1);

		kieServices.newKieBuilder(kfs).buildAll();

		KieContainer kieContainer = kieServices.newKieContainer(kieServices
				.getRepository().getDefaultReleaseId());

		StatelessKieSession kSession = kieContainer.newStatelessKieSession();
		SampleModel sample = new SampleModel("yop");
		kSession.execute(sample);
		System.out.println(sample.isValid());
		Assert.assertTrue(sample.isValid());

	}

	@Test
	public void validator1() {

		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		// Check that the builder works.
		Assert.assertFalse(vBuilder.hasErrors());
		Assert.assertEquals(0, vBuilder.getErrors().size());
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ClassPathResource(
				"action1/rule1.drl", Verifier.class), ResourceType.DRL);
		Assert.assertFalse(verifier.hasErrors());
	}

	@Test
	public void validator2() {

		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		// Check that the builder works.
		Assert.assertFalse(vBuilder.hasErrors());
		Assert.assertEquals(0, vBuilder.getErrors().size());
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ClassPathResource(
				"action1/wrongRule.drl", Verifier.class), ResourceType.DRL);
		Assert.assertTrue(verifier.hasErrors());
		for (VerifierError err : verifier.getErrors()) {
			System.out.println(err.getMessage());
		}

	}

	@Test
	public void validator3() {
		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		// Check that the builder works.
		Assert.assertFalse(vBuilder.hasErrors());
		Assert.assertEquals(0, vBuilder.getErrors().size());
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ClassPathResource(
				"action1/rule2.drl", Verifier.class), ResourceType.DRL);
		Assert.assertFalse(verifier.hasErrors());
		for (VerifierError err : verifier.getErrors()) {
			System.out.println(err.getMessage());
		}
	}

	@Test
	public void validator4() {
		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		// Check that the builder works.
		Assert.assertFalse(vBuilder.hasErrors());
		Assert.assertEquals(0, vBuilder.getErrors().size());
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ClassPathResource(
				"action1/rule3.drl", Verifier.class), ResourceType.DRL);
		Assert.assertFalse(verifier.hasErrors());
		for (VerifierError err : verifier.getErrors()) {
			System.out.println(err.getMessage());
		}
	}

	@Test
	public void validator5() {
		String rule = "package eu.trentorise.drools " + "rule \"valid sample\""
				+ " when" + " eval(true)" + " then"
				+ " insert(new SampleModel('ciao'));" + " end";
		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		// Check that the builder works.
		Assert.assertFalse(vBuilder.hasErrors());
		Assert.assertEquals(0, vBuilder.getErrors().size());
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ByteArrayResource(rule.getBytes())
				.setTargetPath("/fake.drl"), ResourceType.DRL);
		for (VerifierError err : verifier.getErrors()) {
			System.out.println(err.getMessage());
		}
		Assert.assertFalse(verifier.hasErrors());
	}

	@Test
	public void inMemoryRule() {
		String rule = "package eu.trentorise.drools " + "rule \"valid sample\""
				+ " when" + " eval(true)" + " then"
				+ " insert(new SampleModel('ciao'));" + " end";

		String query = "package eu.trentorise.drools query \"GetOutputObj\" "
				+ "$o: SampleModel() " + "end";

		KieServices kieServices = KieServices.Factory.get();

		KieResources res = kieServices.getResources();

		org.kie.api.io.Resource r1 = res.newByteArrayResource(rule.getBytes())
				.setTargetPath("/t.drl");
		org.kie.api.io.Resource r2 = res.newByteArrayResource(query.getBytes())
				.setTargetPath("/t1.drl");
		KieFileSystem kfs = kieServices.newKieFileSystem();
		kfs.write(r1);
		kfs.write(r2);

		kieServices.newKieBuilder(kfs).buildAll();

		KieContainer kieContainer = kieServices.newKieContainer(kieServices
				.getRepository().getDefaultReleaseId());

		StatelessKieSession kSession = kieContainer.newStatelessKieSession();
		SampleModel sample = new SampleModel("yop");

		List<Command> cmds = new ArrayList<Command>();
		cmds.add(CommandFactory.newInsert(sample));
		cmds.add(CommandFactory.newQuery("query", "GetOutputObj"));
		cmds.add(CommandFactory.newFireAllRules());
		ExecutionResults results = kSession.execute(CommandFactory
				.newBatchExecution(cmds));
		Iterator<QueryResultsRow> iter = ((QueryResults) results
				.getValue("query")).iterator();
		boolean ok = false;
		// while (iter.hasNext()) {
		// SampleModel sm = (SampleModel) iter.next().get("$o");
		// ok = ok || sm.getHello().equals("ciao");
		// }
		//
		// Assert.assertTrue(ok);
	}

	@Test
	public void global() {
		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		// Check that the builder works.
		Assert.assertFalse(vBuilder.hasErrors());
		Assert.assertEquals(0, vBuilder.getErrors().size());
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ClassPathResource(
				"action1/globalRule.drl", Verifier.class), ResourceType.DRL);
		Assert.assertFalse(verifier.hasErrors());
	}

	@Test
	public void extObj() {
		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		// Check that the builder works.
		Assert.assertFalse(vBuilder.hasErrors());
		Assert.assertEquals(0, vBuilder.getErrors().size());
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ClassPathResource(
				"action1/extObjRule.drl", Verifier.class), ResourceType.DRL);
		Assert.assertFalse(verifier.hasErrors());
	}

	@Test
	public void templatingRule() throws IOException {
		ObjectDataCompiler compiler = new ObjectDataCompiler();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("chid", "RANDOM_VALUE");
		params.put("player", "bruce_wayne");
		params.put("bonus", 50);

		String ruleResult = "package eu.trentorise.game.model\n"
				+ "// CH1variant - Aumenta del 15% i km fatti a piedi e avrai 50 punti bonus\n"
				+ "/*\n"
				+ " CustomData to have   []=used by\n"
				+ "    ID is a unique string that identifies the challenge instance\n"
				+ "  - chid-startChTs [rule] [presentation]\n"
				+ "  - chid-endChTs [rule] [presentation]\n"
				+ "  - chid-Km_walked_during_challenge [rule] [presentation]\n"
				+ "  - chid-target [rule] [presentation]\n"
				+ "  - chid-bonus [presentation]\n"
				+ "  - chid-type [presentation]\n"
				+ "*/\n"
				+ "\n"
				+ "rule 'ch-RANDOM_VALUE-trace'\n"
				+ "no-loop\n"
				+ "when\n"
				+ "	InputData($walk : data[\"walkDistance\"])\n"
				+ "	$c: CustomData($now: System.currentTimeMillis(), this['ch-RANDOM_VALUE-startChTs'] <= $now, this['ch-RANDOM_VALUE-endChTs'] > $now)\n"
				+ "	Player(id == 'bruce_wayne') \n"
				+ "then\n"
				+ "	Double o = (Double) $c.get('ch-RANDOM_VALUE-Km_walked_during_challenge');\n"
				+ "	if(o == null) {\n"
				+ "		$c.put('ch-RANDOM_VALUE-Km_walked_during_challenge',(Double) $walk);	\n"
				+ "	}else {\n"
				+ "		$c.put('ch-RANDOM_VALUE-Km_walked_during_challenge', o + (Double) $walk);\n"
				+ "	}\n"
				+ "	log('RANDOM_VALUE update');\n"
				+ "	update($c); \n"
				+ "end\n"
				+ "rule 'ch-RANDOM_VALUE-check'\n"
				+ "when\n"
				+ "	Player(id == 'bruce_wayne')\n"
				+ "	$c: CustomData($now: System.currentTimeMillis(), this['ch-RANDOM_VALUE-startChTs'] <= $now, this['ch-RANDOM_VALUE-endChTs'] > $now, this['ch-RANDOM_VALUE-Km_walked_during_challenge'] >= this['ch-RANDOM_VALUE-target'])\n"
				+ "	$pc : PointConcept(name == \"green leaves\")\n" + "then\n"
				+ "	$pc.setScore($pc.getScore() + 50);\n"
				+ "	log('RANDOM_VALUE success');\n" + "	update($pc); \n"
				+ "end";

		System.out
				.println(compiler.compile(
						Arrays.asList(params),
						Thread.currentThread()
								.getContextClassLoader()
								.getResourceAsStream(
										"rules/templates/ruleTemplate.drl")));

	}
}
