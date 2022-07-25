package steelUnicorn.laplacity.field.physics;

import com.badlogic.gdx.physics.box2d.Body;

public class BodyData {

	// id
	public int id;
	
	// body
	public Body body;
	
	// deadly or finish
	public boolean deadly;
	public boolean finish;
	
	// main particle
	public boolean mainParticle;
	
	// structure
	public boolean structure;
	
	public boolean isStructure() {
		return structure;
	}

	public BodyData() {
	}

	public void setStructure(boolean structure) {
		this.structure = structure;
	}

	public void setMainParticle(boolean mainParticle) {
		this.mainParticle = mainParticle;
	}

	public BodyData(int id) {
		super();
		this.id = id;
	}

	public BodyData(Body body) {
		super();
		this.body = body;
	}

	public void setDeadly(boolean deadly) {
		this.deadly = deadly;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public int getId() {
		return id;
	}

	public Body getBody() {
		return body;
	}

	public boolean isDeadly() {
		return deadly;
	}

	public boolean isFinish() {
		return finish;
	}

	public boolean isMainParticle() {
		return mainParticle;
	}

	@Override
	public String toString() {
		return "BodyData [id=" + id + ", body=" + body + ", deadly=" + deadly + ", finish=" + finish + ", mainParticle="
				+ mainParticle + ", structure=" + structure + "]";
	}

	public static BodyData createForStructure() {
		BodyData dat = new BodyData();
		dat.setStructure(true);
		return dat;
	}
	
}
