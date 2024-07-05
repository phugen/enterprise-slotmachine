package phugen.slotmachine;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import phugen.slotmachine.service.interfaces.Slotmachine;

@SpringBootApplication
public class SlotmachineApplication {

	private final Slotmachine slotmachine;

	public SlotmachineApplication(Slotmachine slotmachine) {
		this.slotmachine = slotmachine;
	}

	public static void main(String[] args) {
		SpringApplication.run(SlotmachineApplication.class, args);
	}

	@PostConstruct
	private void run() {
		slotmachine.play();
	}
}
