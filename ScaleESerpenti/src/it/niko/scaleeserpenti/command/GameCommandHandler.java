package it.niko.scaleeserpenti.command;

public class GameCommandHandler implements CommandHandler {

	private static GameCommandHandler INSTANCE = null;

	private GameCommandHandler() {}

	public static GameCommandHandler getINSTANCE() {
		if(INSTANCE == null)
			INSTANCE = new GameCommandHandler();
		return INSTANCE;
	}

	public void handle(Command cmd) {
		cmd.execute();
	}
}
