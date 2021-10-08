package it.niko.game.command;

public class GameCommandHandler implements CommandHandler {

	public GameCommandHandler() {
		super();
	}

	public void handle(Command cmd) {
		cmd.execute();
	}
}
