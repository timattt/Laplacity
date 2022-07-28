package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import steelUnicorn.laplacity.GameProcess;

public class RigidTextureLoader {
	
	public static Body createSolidTexture(String path, float scale, Vector2 origin, String name) {
		//  0.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(path));
		
		 // 1. Create a BodyDef, as usual.
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DynamicBody;

        // 2. Create a FixtureDef, as usual.
        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.3f;

        // 3. Create a Body, as usual.
        Body body = GameProcess.registerPhysicalObject(bd);

        // 4. Create the body fixture automatically by using the loader.
        loader.attachFixture(body, name, fd, scale);
        origin.set(loader.getOrigin(name, scale));
        
        return body;
	}
	
}
