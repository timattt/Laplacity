package steelUnicorn.laplacity.gameModes;

public class GameMode {

	// MODES
	public static final GameMode NONE = new ModeNone();
	public static final GameMode FLIGHT = new ModeFlight();
	public static final GameMode DIRICHLET = new ModeDirichlet();
	public static final GameMode ERASER = new ModeEraser();
	public static final GameMode ELECTRONS = new ModeElectrons();
	public static final GameMode PROTONS = new ModeProtons();
	
	// name
	private String name;
	
	public GameMode(String name) {
		this.name = name;
	}
	
	public void tap(float x, float y) {
		
	}
	
	public void pan(float x, float y, float dx, float dy) {
		
	}
	
	public void touchDown(float x, float y) {
		
	}
	
	public void touchUp(float x, float y) {
		
	}
	
	public void touchDragged(float x, float y) {
		
	}
	
	public void pinch(float dx1, float dx2) {
		
	}
	
	public void update() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public void replaced() {
		
	}

}
