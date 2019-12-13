package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ARActivity extends AppCompatActivity {

    private ArFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_layout);
        fragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        fragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                int order = getIntent().getIntExtra("order", 0);

                if (order == 1) {
                    makeSphere(hitResult);
                } else if (order == 2) {
                    makeCylinder(hitResult);
                } else if (order == 3) {
                    makeCube(hitResult);
                } else if (order == 4) {
                    makeTextureSphere(hitResult);
                }

            }
        });
    }


    /**
     * 在0.0f、0.15f、0.0f位置构造立方体，并具有纹理
     *
     * @param hitResult - If the hit result is a plane
     */
    private void makeTextureSphere(HitResult hitResult) {

        Texture.builder().setSource(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build()
                .thenAccept(texture -> {
                    MaterialFactory.makeOpaqueWithTexture(this, texture)
                            .thenAccept(material -> {
                                addNodeToScene(fragment, hitResult.createAnchor(),
                                        ShapeFactory.makeCube(new Vector3(0.2f, 0.2f, 0.2f),
                                                new Vector3(0.0f, 0.15f, 0.0f), material)

                                );
                            });

                })
                .exceptionally(throwable -> {
                    Toast toast = Toast.makeText(this, "报错==" + throwable.getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
    }

    //ShapeFactory 的三个方法
    //1.makeSphere 创建一个球
    //2.makeCube 创建一个立方体
    //3.makeCylinder 创建一个圆柱

    private void makeSphere(HitResult hitResult) {
        MaterialFactory.makeOpaqueWithColor(this, new com.google.ar.sceneform.rendering.Color(android.graphics.Color.BLUE))
                .thenAccept(material -> {

                    addNodeToScene(fragment, hitResult.createAnchor(),
                            ShapeFactory.makeSphere(0.1f,
                                    new Vector3(0.0f, 0.15f, 0.0f), material));
                });
    }

    private void makeCylinder(HitResult hitResult) {
        MaterialFactory.makeOpaqueWithColor(this, new com.google.ar.sceneform.rendering.Color(android.graphics.Color.BLUE))
                .thenAccept(material -> {

                    addNodeToScene(fragment, hitResult.createAnchor(),
                            ShapeFactory.makeCylinder(0.1f,
                                    0.3f, new Vector3(0.0f, 0.15f, 0.0f), material));
                });
    }

    private void makeCube(HitResult hitResult) {
        MaterialFactory.makeOpaqueWithColor(this, new com.google.ar.sceneform.rendering.Color(android.graphics.Color.BLUE))
                .thenAccept(material -> {

                    addNodeToScene(fragment, hitResult.createAnchor(),
                            ShapeFactory.makeCube(new Vector3(0.2f, 0.2f, 0.2f),
                                    new Vector3(0.0f, 0.15f, 0.0f), material));
                });
    }


    /**
     * 将节点添加到场景和对象
     *
     * @param fragment    - sceneform fragment
     * @param anchor      - created anchor at the tapped position
     * @param modelObject - rendered object
     */
    private void addNodeToScene(ArFragment fragment, Anchor anchor, ModelRenderable modelObject) {

        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode andy = new TransformableNode(fragment.getTransformationSystem());
        andy.setParent(anchorNode);
        andy.setRenderable(modelObject);
        andy.select();

        fragment.getArSceneView().getScene().addChild(anchorNode);
    }

}
