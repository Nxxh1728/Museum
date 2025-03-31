package museum;

import java.awt.Font;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.LineStripArray;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Morph;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.QuadArray;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TriangleStripArray;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.geometry.Text2D;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;


import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.TexCoord2f;

public class SpaceRoom {
	
	public static Morph sunMorphNode;
	
	public static BranchGroup createSpaceRoom(){
		//Create main BG and TGs
		BranchGroup spaceRoomBG = new BranchGroup();
		TransformGroup solarSystemTG = new TransformGroup();
		solarSystemTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Sun
		Appearance sunApp = createSunApp();
		
		GeometryArray sphere = createSunSphere();
		GeometryArray cube = createSunDiamond();
		
		Morph sunMorph = new Morph(new GeometryArray[] {sphere, cube});
		sunMorph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		sunMorph.setWeights(new double[] {1.0f, 0.0f});
		sunMorph.setAppearance(sunApp);
		sunMorphNode = sunMorph;
		
		//Position the Sun
		Transform3D sunTr = new Transform3D();
		sunTr.setTranslation(new Vector3f(0.0f, 0.5f, 0.0f));
		TransformGroup sunTG = new TransformGroup(sunTr);
		sunTG.addChild(sunMorph);
		solarSystemTG.addChild(sunTG);
		
		//Create TG for Mercury's orbit
		TransformGroup mercuryOrbitTG = new TransformGroup();
		mercuryOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Mercury's orbit ring
		Shape3D mercuryRing = createOrbitRing(0.2f, 0.5f);
		mercuryOrbitTG.addChild(mercuryRing);
		
		//Create Mercury
		Appearance mercuryApp = createApp("mercury");
		Sphere mercury = new Sphere(0.02f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, mercuryApp);
		
		//Position Mercury
		Transform3D mercuryTr = new Transform3D();
		mercuryTr.setTranslation(new Vector3f(0.2f, 0.5f, 0.0f));
		TransformGroup mercuryTG = new TransformGroup(mercuryTr);
		mercuryTG.addChild(mercury);
		mercuryOrbitTG.addChild(mercuryTG);
		
		//Create rotation interpolator for Mercury
		Transform3D orbitAxis = new Transform3D();
		Alpha mercuryAlpha = new Alpha(-1, 15000);
		RotationInterpolator  mercuryRotator = new RotationInterpolator(mercuryAlpha, mercuryOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		mercuryRotator.setSchedulingBounds(bounds);
		mercuryOrbitTG.addChild(mercuryRotator);
		
		solarSystemTG.addChild(mercuryOrbitTG);
		
		//Create TG for Venus' orbit
		TransformGroup venusOrbitTG = new TransformGroup();
		venusOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Venus' orbit ring
		Shape3D venusRing = createOrbitRing(0.4f, 0.5f);
		venusOrbitTG.addChild(venusRing);
		
		//Create Venus
		Appearance venusApp = createApp("venus");
		Sphere venus = new Sphere(0.025f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, venusApp);
		
		//Position Venus
		Transform3D venusTr = new Transform3D();
		venusTr.setTranslation(new Vector3f(0.4f, 0.5f, 0.0f));
		TransformGroup venusTG = new TransformGroup(venusTr);
		venusTG.addChild(venus);
		venusOrbitTG.addChild(venusTG);
		
		//Create rotation interpolator for Venus
		Alpha venusAlpha = new Alpha(-1, 20000);
		RotationInterpolator venusRotator = new RotationInterpolator(venusAlpha, venusOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		venusRotator.setSchedulingBounds(bounds);
		venusOrbitTG.addChild(venusRotator);
		
		solarSystemTG.addChild(venusOrbitTG);
		
		//Create TG for Earth's orbit
		TransformGroup earthOrbitTG = new TransformGroup();
		earthOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Earth's orbit ring
		Shape3D earthRing = createOrbitRing(0.6f, 0.5f);
		earthOrbitTG.addChild(earthRing);
		
		//Create Earth
		Appearance earthApp = createApp("earth");
		Sphere earth = new Sphere(0.04f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, earthApp);

		//Position Earth
		Transform3D earthTr = new Transform3D();
		earthTr.setTranslation(new Vector3f(0.6f, 0.5f, 0.0f));
		TransformGroup earthTG = new TransformGroup(earthTr);
		earthTG.addChild(earth);
		earthOrbitTG.addChild(earthTG);
		
		//Create rotation interpolator for Earth
		Alpha earthAlpha = new Alpha(-1, 25000);
		RotationInterpolator earthRotator = new RotationInterpolator(earthAlpha, earthOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		earthRotator.setSchedulingBounds(bounds);
		earthOrbitTG.addChild(earthRotator);
		
		solarSystemTG.addChild(earthOrbitTG);
		
		//Create TG for Mars' orbit
		TransformGroup marsOrbitTG = new TransformGroup();
		marsOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Mars' orbit ring
		Shape3D marsRing = createOrbitRing(0.8f, 0.5f);
		marsOrbitTG.addChild(marsRing);
			
		//Create Mars
		Appearance marsApp = createApp("mars");
		Sphere mars = new Sphere(0.03f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, marsApp);
				
		//Position Mars
		Transform3D marsTr = new Transform3D();
		marsTr.setTranslation(new Vector3f(0.8f, 0.5f, 0.0f));
		TransformGroup marsTG = new TransformGroup(marsTr);
		marsTG.addChild(mars);
		marsOrbitTG.addChild(marsTG);
				
		//Create rotation interpolator for Mars
		Alpha marsAlpha = new Alpha(-1, 30000);
		RotationInterpolator marsRotator = new RotationInterpolator(marsAlpha, marsOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		marsRotator.setSchedulingBounds(bounds);
		marsOrbitTG.addChild(marsRotator);
				
		solarSystemTG.addChild(marsOrbitTG);
		
		//Create TG for Jupiter's orbit
		TransformGroup jupiterOrbitTG = new TransformGroup();
		jupiterOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Jupiter's orbit ring
		Shape3D jupiterRing = createOrbitRing(1.0f, 0.5f);
		jupiterOrbitTG.addChild(jupiterRing);
				
		//Create Jupiter
		Appearance jupiterApp = createApp("jupiter");
		Sphere jupiter = new Sphere(0.06f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, jupiterApp);
				
		//Position Jupiter
		Transform3D jupiterTr = new Transform3D();
		jupiterTr.setTranslation(new Vector3f(1.0f, 0.5f, 0.0f));
		TransformGroup jupiterTG = new TransformGroup(jupiterTr);
		jupiterTG.addChild(jupiter);
		jupiterOrbitTG.addChild(jupiterTG);
				
		//Create rotation interpolator for Jupiter
		Alpha jupiterAlpha = new Alpha(-1, 35000);
		RotationInterpolator jupiterRotator = new RotationInterpolator(jupiterAlpha, jupiterOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		jupiterRotator.setSchedulingBounds(bounds);
		jupiterOrbitTG.addChild(jupiterRotator);
				
		solarSystemTG.addChild(jupiterOrbitTG);
		
		//Create TG for Saturn's orbit
		TransformGroup saturnOrbitTG = new TransformGroup();
		saturnOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Saturn's orbit ring
		Shape3D saturnRing = createOrbitRing(1.2f, 0.5f);
		saturnOrbitTG.addChild(saturnRing);
				
		//Create Saturn
		Appearance saturnApp = createApp("saturn");
		Sphere saturn = new Sphere(0.055f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, saturnApp);
				
		//Position Saturn
		Transform3D saturnTr = new Transform3D();
		saturnTr.setTranslation(new Vector3f(1.2f, 0.5f, 0.0f));
		TransformGroup saturnTG = new TransformGroup(saturnTr);
		saturnTG.addChild(saturn);
		saturnOrbitTG.addChild(saturnTG);
				
		//Create rotation interpolator for Saturn
		Alpha saturnAlpha = new Alpha(-1, 40000);
		RotationInterpolator saturnRotator = new RotationInterpolator(saturnAlpha, saturnOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		saturnRotator.setSchedulingBounds(bounds);
		saturnOrbitTG.addChild(saturnRotator);
				
		solarSystemTG.addChild(saturnOrbitTG);
		
		//Create TG for Uranus' orbit
		TransformGroup uranusOrbitTG = new TransformGroup();
		uranusOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Urans' orbit ring
		Shape3D uranusRing = createOrbitRing(1.4f, 0.5f);
		uranusOrbitTG.addChild(uranusRing);
				
		//Create Uranus
		Appearance uranusApp = createApp("uranus");
		Sphere uranus = new Sphere(0.05f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, uranusApp);
				
		//Position Uranus
		Transform3D uranusTr = new Transform3D();
		uranusTr.setTranslation(new Vector3f(1.4f, 0.5f, 0.0f));
		TransformGroup uranusTG = new TransformGroup(uranusTr);
		uranusTG.addChild(uranus);
		uranusOrbitTG.addChild(uranusTG);
				
		//Create rotation interpolator for Uranus
		Alpha uranusAlpha = new Alpha(-1, 45000);
		RotationInterpolator uranusRotator = new RotationInterpolator(uranusAlpha, uranusOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		uranusRotator.setSchedulingBounds(bounds);
		uranusOrbitTG.addChild(uranusRotator);
				
		solarSystemTG.addChild(uranusOrbitTG);
		
		//Create TG for Neptune's orbit
		TransformGroup neptuneOrbitTG = new TransformGroup();
		neptuneOrbitTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Create Neptune's orbit ring
		Shape3D neptuneRing = createOrbitRing(1.6f, 0.5f);
		neptuneOrbitTG.addChild(neptuneRing);
				
		//Create Neptune
		Appearance neptuneApp = createApp("neptune");
		Sphere neptune = new Sphere(0.045f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, neptuneApp);
	
		//Position Neptune
		Transform3D neptuneTr = new Transform3D();
		neptuneTr.setTranslation(new Vector3f(1.6f, 0.5f, 0.0f));
		TransformGroup neptuneTG = new TransformGroup(neptuneTr);
		neptuneTG.addChild(neptune);
		neptuneOrbitTG.addChild(neptuneTG);
				
		//Create rotation interpolator for Neptune
		Alpha neptuneAlpha = new Alpha(-1, 50000);
		RotationInterpolator neptuneRotator = new RotationInterpolator(neptuneAlpha, neptuneOrbitTG, orbitAxis, 0.0f, (float)(2*Math.PI));
		neptuneRotator.setSchedulingBounds(bounds);
		neptuneOrbitTG.addChild(neptuneRotator);
				
		solarSystemTG.addChild(neptuneOrbitTG);
		
		//Create chandelier pole
		Appearance poleApp = new Appearance();
		Material poleMat = new Material();
		poleMat.setDiffuseColor(new Color3f(0.894f, 0.839f, 0.106f));
		poleMat.setSpecularColor(new Color3f(0.894f, 0.839f, 0.106f));
		poleMat.setShininess(64.0f);
		poleApp.setMaterial(poleMat);
		
		Cylinder pole = new Cylinder(0.01f, 0.8f, Cylinder.GENERATE_NORMALS, poleApp);
		pole.getShape(0).setAppearance(poleApp);
		pole.getShape(1).setAppearance(poleApp);
		pole.getShape(2).setAppearance(poleApp);
		Transform3D poleTr = new Transform3D();
		poleTr.setTranslation(new Vector3f(0.0f, 1.0f, 0.0f));
		TransformGroup poleTG = new TransformGroup(poleTr);
		poleTG.addChild(pole);
		solarSystemTG.addChild(poleTG);
		
		
		Transform3D ceilingTr = new Transform3D();
		ceilingTr.setTranslation(new Vector3f(0.0f, 0.15f, 0.0f));
		Shape3D ceiling = createCeiling();
		TransformGroup ceilingTG = new TransformGroup(ceilingTr);
		ceilingTG.addChild(ceiling);
		solarSystemTG.addChild(ceilingTG);
		//Translate whole solar system
		Transform3D roomTr = new Transform3D();
		roomTr.setTranslation(new Vector3f(-3.0f, 0.4f, -4.0f));
		TransformGroup roomTG = new TransformGroup(roomTr);
		roomTG.addChild(solarSystemTG);
		roomTG.addChild(createText());
		
		spaceRoomBG.addChild(roomTG);
		return spaceRoomBG;
	}
	
	public static Shape3D createOrbitRing(float radius, float yOffset) {
		int segments = 100;
		LineStripArray ringGeometry = new LineStripArray(segments+1, GeometryArray.COORDINATES, new int[] {segments+1});
		for (int i = 0; i<= segments; i++) {
			double angle = 2 * Math.PI * i / segments;
			float x = (float)(radius * Math.cos(angle));
			float y = (float)(radius * Math.sin(angle));
			ringGeometry.setCoordinate(i, new Point3f(x, yOffset, y));
		}
		
		Appearance ringApp = new Appearance();
		ColoringAttributes ca = new ColoringAttributes(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.NICEST);
		ringApp.setColoringAttributes(ca);
		return new Shape3D(ringGeometry, ringApp);
	}
	
	public static Appearance createApp(String s) {
		String imagePath = "images/"+s+".jpeg";
		TextureLoader loader = new TextureLoader(imagePath, null);
		ImageComponent2D planetImage = loader.getImage();
		if (planetImage == null) {
			System.out.println("Error loading texture for"+s);
		}
		Texture2D planetTexture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGBA, planetImage.getWidth(), planetImage.getHeight());
		planetTexture.setImage(0, planetImage);
		Appearance planetApp = new Appearance();
		planetApp.setTexture(planetTexture);
		TextureAttributes texAtt = new TextureAttributes();
		texAtt.setTextureMode(TextureAttributes.REPLACE);
		planetApp.setTextureAttributes(texAtt);
		return planetApp;
	}
	
	public static Appearance createSunApp() {
		Appearance app = createApp("sun");
		PolygonAttributes pa = new PolygonAttributes();
		pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		app.setPolygonAttributes(pa);
		return app;
	}
	
	public static Shape3D createCeiling() {
		float width = 4.0f;
		float depth = 4.0f;
		float y = 0.95f;
		
		Point3f[] vertices = new Point3f[] {
				new Point3f(-width/2, y, -depth/2),
				new Point3f(width/2, y, -depth/2),
				new Point3f( width/2, y,  depth/2),
		        new Point3f(-width/2, y,  depth/2)
		};
		
		 QuadArray quad = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);
		 quad.setCoordinates(0, vertices);
		 
		 TexCoord2f[] texCoords = new TexCoord2f[] {
				 new TexCoord2f(0.0f, 0.0f),
			     new TexCoord2f(1.0f, 0.0f),
			     new TexCoord2f(1.0f, 1.0f),
			     new TexCoord2f(0.0f, 1.0f)
		};
		 quad.setTextureCoordinate(0, 0, texCoords[0]);
		 quad.setTextureCoordinate(0, 1, texCoords[1]);
		 quad.setTextureCoordinate(0, 2, texCoords[2]);
		 quad.setTextureCoordinate(0, 3, texCoords[3]);
		 
		 Appearance ceilingApp = new Appearance();
		 TextureLoader loader = new TextureLoader("images/stars.jpeg", null);
		 ImageComponent2D image = loader.getImage();
		 if (image == null) {
			 System.out.println("Couldn't load stars image");
		 }
		 Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGBA, image.getWidth(), image.getHeight());
		 texture.setImage(0, image);
		 ceilingApp.setTexture(texture);
		 
		 TextureAttributes textAtt = new TextureAttributes();
		 textAtt.setTextureMode(TextureAttributes.MODULATE);
		 ceilingApp.setTextureAttributes(textAtt);
		 
		 PolygonAttributes pa = new PolygonAttributes();
		  pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		  pa.setCullFace(PolygonAttributes.CULL_NONE);
		  ceilingApp.setPolygonAttributes(pa);
		 
		 return new Shape3D(quad, ceilingApp);
	}
	
	public static TransformGroup createText() {
		Text2D text2d = new Text2D("Press P to morph the Sun",
									new Color3f(1.0f, 1.0f, 1.0f),
									"Helvetica", 24, Font.BOLD);
		Transform3D textTr = new Transform3D();
		textTr.setTranslation(new Vector3f(-0.4f, 0f, -1.8f));
		TransformGroup textTG = new TransformGroup(textTr);
		textTG.addChild(text2d);
		return textTG;
	}
	
	public static GeometryArray createSunSphere() {
		int lat = 20;
		int lon = 20;
		int strips = lat;
		
		int[] stripCounts = new int[strips];
		for (int i = 0; i<strips; i++) {
			stripCounts[i] = 2 * (lon + 1);
		}
		int totVertices = 0;
		for (int i = 0; i<strips; i++) {
			totVertices += stripCounts[i];
		}
		
		TriangleStripArray sphereArray = new TriangleStripArray(totVertices, GeometryArray.COORDINATES, stripCounts);
		int vertIndex = 0;
		// Loop over latitude bounds
		for (int i = 0; i<lat; i++) {
			double theta1 = Math.PI * i/lat;
			double theta2 = Math.PI * (i+1)/lat;
			// Loop over longitude
			for (int j = 0; j <= lon; j++) {
				double phi = 2 * Math.PI * j / lon;
				float x1 = (float)(Math.sin(theta1) * Math.cos(phi));
				float y1 = (float)(Math.cos(theta1));
				float z1 = (float)(Math.sin(theta1) * Math.sin(phi));
				sphereArray.setCoordinate(vertIndex++, new Point3f(x1, y1, z1));
				
				float x2 = (float)(Math.sin(theta2) * Math.cos(phi));
				float y2 = (float)(Math.cos(theta2));
				float z2 = (float)(Math.sin(theta2) * Math.sin(phi));
				sphereArray.setCoordinate(vertIndex++, new Point3f(x2, y2, z2));
				
			}
		}
		//Scale sphere
		Transform3D scale = new Transform3D();
		scale.setScale(0.08f);
		for (int i = 0; i < totVertices; i++) {
			Point3f p = new Point3f();
			sphereArray.getCoordinate(i, p);
			scale.transform(p);
			sphereArray.setCoordinate(i, p);
		}
		return sphereArray;
	}
	
	public static GeometryArray createSunDiamond() {
		GeometryArray sphereArray = createSunSphere();
		int vertexCount = sphereArray.getVertexCount();
		
		int lat = 20;
		int lon = 20;
		int strips = lat;
		int[] stripCounts = new int[strips];
		for (int i = 0; i < strips; i++) {
			stripCounts[i] = 2 * (lon + 1);
		}
		
		TriangleStripArray cubeArray = new TriangleStripArray(vertexCount, GeometryArray.COORDINATES, stripCounts);
		float scale = 0.08f;
		for (int i = 0; i < vertexCount; i++) {
			Point3f p = new Point3f();
			sphereArray.getCoordinate(i, p);
			
			float sum = Math.abs(p.x) + Math.abs(p.y) + Math.abs(p.z);
	        if (sum == 0) {
	            sum = 1; 
	        }
	        float newX = (p.x / sum) * scale;
	        float newY = (p.y / sum) * scale;
	        float newZ = (p.z / sum) * scale;
			
			cubeArray.setCoordinate(i, new Point3f(newX, newY, newZ));
		}
		return cubeArray;
	}

}
