package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 * test
 * @author normenhansen
 */
public class mesh_drawing extends SimpleApplication {
    
    public mesh_drawing(Vector3f[] lineVerticies, int simulation_position, float move_scale, float[] isworking_data){
        my_lineVerticies=new Vector3f[simulation_position];
        System.arraycopy(lineVerticies, 0, my_lineVerticies, 0, simulation_position);
        all_my_lineVerticies=lineVerticies;
        my_move_scale=move_scale;
        my_isworking_data=new float[3*simulation_position];
        System.arraycopy(isworking_data, 0, my_isworking_data, 0, 3*simulation_position);
    }
    
    public void refresh_data(Vector3f[] lineVerticies, int simulation_position, float[] isworking_data){
        my_lineVerticies=new Vector3f[simulation_position];
        System.arraycopy(lineVerticies, 0, my_lineVerticies, 0, simulation_position);
        my_isworking_data=new float[3*simulation_position];
//        System.out.println(simulation_position);
//        System.out.println(my_isworking_data.length);
        System.arraycopy(isworking_data, 0, my_isworking_data, 0, 3*simulation_position);
        this.my_simpleInitApp();
        isrefresh_needed=true;
    }
    
    public void my_simpleInitApp()
    {
        
    }

    @Override
    public void simpleInitApp() {
        
        inputManager.addMapping("focus", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener,"focus");
        
        plotLine(my_lineVerticies, ColorRGBA.Blue, my_isworking_data);
        
        flyCam.setMoveSpeed(my_move_scale);
    }
    
    public void plotLine(Vector3f[] lineVerticies, ColorRGBA lineColor, float[] isworking_data){
        Mesh m = new Mesh();
        m.setMode(Mesh.Mode.Lines);


        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));

        int[] indexes=new int[2*lineVerticies.length]; //Indexes are in pairs, from a vertex and to a vertex

        for(int i=0;i<lineVerticies.length-1;i++){
            indexes[2*i]=i;
            indexes[2*i+1]=(int)(i+1);
        }

        m.setBuffer(VertexBuffer.Type.Index, 2, indexes);

        m.updateBound();
        m.updateCounts();

        Geometry geo=new Geometry("line",m);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setBoolean("VertexColor", true);
        
        m.setBuffer(Type.Color, 3, isworking_data);
        geo.setMaterial(mat);
        
        //mat.setColor("Color", lineColor);
        //geo.setMaterial(mat);

        rootNode.attachChild(geo);
    }
    
 private ActionListener actionListener = new ActionListener() {
  public void onAction(String name, boolean keyPressed, float tpf) {
     if(name.equals("BUTTON_LEFT") || keyPressed==false)
     {
         if(inputManager.isCursorVisible()==false)
         {
             inputManager.setCursorVisible(true);
             flyCam.setEnabled(false);
         }
         else
         {
             inputManager.setCursorVisible(false);
             flyCam.setEnabled(true);
         }
         
     }
  }
};

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        if (isrefresh_needed)
        {
            rootNode.detachAllChildren();
            plotLine(my_lineVerticies,ColorRGBA.Blue, my_isworking_data);
            isrefresh_needed=false;
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    float my_move_scale;
    Vector3f[] my_lineVerticies;
    boolean isrefresh_needed=false;
    Vector3f[] all_my_lineVerticies;
    float[] my_isworking_data;
}
