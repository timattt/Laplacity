package steelUnicorn.laplacity.utils;

public interface DebugHandler {

	public void debugMessage(String text);
	public boolean isDebugModeEnabled();
	public boolean allLevelsOpened();
	public boolean isPlayerCheater();
	
}
