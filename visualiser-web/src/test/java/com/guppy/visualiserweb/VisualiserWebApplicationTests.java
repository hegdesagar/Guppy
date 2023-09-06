package com.guppy.visualiserweb;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.guppy.visualiser.consumermq.KafkaMessageConsumer;
import com.guppy.visualiserweb.controller.VisualiserController;
import com.guppy.visualiserweb.data.model.SimulationOptions;

@SpringBootTest
class VisualiserWebApplicationTests {
	
	@MockBean
	private KafkaMessageConsumer kafkaMessageConsumer;

	@Test
	void contextLoads() {
	}
	
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	public class AuthenticatedEchoBroadcastStrategyTest {

	    @Autowired
	    private SimpMessagingTemplate template;

	    @Autowired
	    private VisualiserController controller; 
	    
	    @Test
	    public void testSimulate() throws Exception {
	        SimulationOptions options = new SimulationOptions();
	        options.setNodes(5);
	        options.setImplementation("AuthenticatedEchoBroadcastStrategy");
	        options.setTimeline(10);
	        options.setFaults(1);

	        // Call the simulate method
	        controller.simulate(options);

	        //Mockito.when(kafkaMessageConsumer.consume()).thenReturn(yourMockedRecords);

	    }
	}

}
