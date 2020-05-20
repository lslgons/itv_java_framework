package com.cj.tvui.controller;

import com.cj.tvui.util.LOG;
import com.cj.tvui.util.RemoteClassLoader;
import com.cj.tvui.Constants;
import com.cj.tvui.dmc.interfaces.DisplayInterface;
import com.cj.tvui.ui.DiagnosticScene;
import com.cj.tvui.ui.Scene;

import java.awt.Container;
import java.awt.event.KeyListener;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 화면 제어 클래스
 * @author daegon.kim (daegon.kim1@cj.net)
 * @since 2016-12-02
 */
public final class SceneController{
    private static SceneController instance = null;

    public static SceneController getInstance() {
        if(instance == null) instance = new SceneController();
        return instance;
    }

    Container rootScene = null;
    //Stack에는 이전에 출력하였던 Scene들...
    //현재 활성화된 Scene은 currentScene
    private Stack sceneStack = new Stack();
    //2018-11-01, 현재 활성화된 scene 패키지 경로명, 중복 호출을 방지하기 위함
    private Stack scenePkgNameStack = new Stack();
    private String currentScenePkgPath=null;
    private Scene currentScene = null;
    private Scene staticScene = null; // 공통으로 사용되는 scene //20min add.
    private DiagnosticScene diagScene = null;
    /**
     * DMC별 UI레이아웃 설정을 위한 인터페이스 객체
     */

    DisplayInterface dispInterface = null;
    
    //Timer
    private Timer scheduleTimer=null;
    private long schduleTime = 1000L;
    private boolean isStartTimer = false;
    //private Thread timerThread;
    private SceneController(){

        LOG.print("Scene Controller Created");
    	dispInterface = RemoteClassLoader.loadDisplayInterface();
    	rootScene = dispInterface.getScene();
    	rootScene.setBounds(0,0,Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
    	if(Constants.ENABLE_DIAGNOSTIC) {
    		diagScene = new DiagnosticScene();
    	}
    	if(Constants.ENABLE_DIAGNOSTIC) {
    		rootScene.add(diagScene, -1);
    	}

    	if(!isStartTimer) {
            scheduleTimer = new Timer();
            scheduleTimer.schedule(new TimerTask() {
                public void run() {
                    if(isStartTimer && currentScene != null) {
                        currentScene._timerWentOff();
                        if(Constants.ENABLE_DIAGNOSTIC) {
                            diagScene._timerWentOff();
                        }
                    }
                }
            }, schduleTime, schduleTime);
            isStartTimer = true;
        }

    }
    
    public void shutdown() {
        if(rootScene != null) {
            rootScene.setVisible(false);
            rootScene.removeAll();
            if(currentScene != null) {
                currentScene._onDestroy();
                currentScene=null;
            }

            //scheduleTimer.purge();
            if(scheduleTimer != null) {
                scheduleTimer.cancel();
                scheduleTimer = null;
            }

            if(sceneStack != null) {
                System.out.println("[Scene Controller] current loaded scene : "+sceneStack.size());
                int count=0;
                while(!sceneStack.empty()) {
                    System.out.println("[Scene Controller] release scene: "+(++count));
                    Scene s= (Scene) sceneStack.pop();
                    s._onDestroy();
                    s=null;
                }
                sceneStack.clear();
                sceneStack=null;
            }
            if(scenePkgNameStack != null) {
                scenePkgNameStack.clear();
                scenePkgNameStack=null;
            }
        }
        dispInterface=null;
        instance=null;



    	
    }

    /**
     * 최상위 Container 반환 (플랫폼 마다 다름)
     * @return rootScene
     */
    public Container getRootScene() {return this.rootScene;}

    /**
     * 타이머 재시작
     */
    public void resumeTimer() {
        if(!isStartTimer) isStartTimer=true;
    }

    /**
     * 타이머 중지
     */
    public void stopTimer() {
        if(isStartTimer) isStartTimer = false;
    }

    /**
     * StaticScene 설정
     * @param classPath Scene클래스 절대경로
     * @param args 전달할 데이터 배열
     */
    public void setStaticScene(final String classPath, Object[] args) {  //공통으로 사용될 최상위 Scene..20min add..
    	 try {
             if (rootScene == null) throw new RuntimeException("Root Scene is null");
             LOG.print(this, "static Scene from "+classPath);
             Scene scene = (Scene) RemoteClassLoader.loadClass(classPath).newInstance();
             
             staticScene = scene;

			// rootScene.add(commonScene);
			rootScene.add(staticScene, 0); // 최상위로..
			staticScene.setVisible(true);
			staticScene.onShow();
			staticScene.setIsStatic(true);
			rootScene.repaint();
      
         } catch (InstantiationException e) {
             e.printStackTrace();
         } catch (IllegalAccessException e) {
             e.printStackTrace();
         }
    }

    /**
     * 설정된 Static Scene 가져옴
     * @return Scene
     * @see Scene
     */
    public Scene getStaticScene() {
    	return this.staticScene;
    }

    /**
     * StaticScene을 보여줄지 설정
      * @param isVisible true면 활성화
     */
    public void showStaticScene(boolean isVisible) { // 공통으로 사용되는 commonScene의 Visible 제어.
//    	System.out.println(rootScene.getComponent(0).toString()+"===========");//cjtcom.StaticScene[,0,0,960x540]
//    	System.out.println("["+rootScene.getComponent(0).getClass().toString()+"]");
    	if(staticScene != null) {
    		staticScene.setVisible(isVisible);
        	if(isVisible) {
        		staticScene.onShow();
        	} else {
        		staticScene.onHide();
        	}
    	}
    	
//		if (rootScene.getComponent(0).getClass().toString().trim().equals("class cjtcom.StaticScene")) {
//			rootScene.getComponent(0).setVisible(isVisible);
//		}
    	
//    	Scene scene =(Scene)rootScene.getComponent(0);
//    	scene.onShow();
//    	scene.onInit();
    }

    /**
     * StaticScene 제거
     */
    public void removeStaticScene() { // 완전제거한다. //20min add..
    	 if (rootScene != null && staticScene != null) {
    		 staticScene._onDestroy();
    		 rootScene.remove(staticScene);
    		 staticScene = null;
    	 }
    }

    /**
     * StaticScene 설정 여부
     * @return true면 StaticScene설정 됨
     */
    public boolean isSetStaticScene() {
    	if(staticScene == null) return false;
    	return true;
    }

    /**
     * StaticScene이 활성화되어있는 반환
     * @return true면 활성화
     */
    public boolean isShowStaticScene() {
    	if(staticScene == null) return false;
    	return staticScene.isVisible();
    }

    /**
     * 현재 Scene에서 다른 Scene으로 전환
     * @param classPath Scene클래스 절대경로
     * @param args 전달할 데이터
     */
    public void pushScene(final String classPath, Object[] args) {
        try {
            if (rootScene == null) throw new RuntimeException("Root Scene is null");
            LOG.print(this, "Push Scene from "+classPath);

            //중복된 Scene 호출방지
            if(currentScenePkgPath!=null && currentScenePkgPath.equalsIgnoreCase(classPath)) {
                LOG.print(this, "duplicated request scene, skip push scene!!!");
                return;
            }

            Class sc = RemoteClassLoader.loadClass(classPath);
            if(sc == null) {
                LOG.print("Null Loaded class... Stop push scene");
                return;
            }
            Scene scene = (Scene) sc.newInstance();
            
            if(currentScene != null) {
                this.sceneStack.push(currentScene);
                this.scenePkgNameStack.push(currentScenePkgPath);
                currentScene.setVisible(false);
                currentScene.onHide();
                rootScene.remove(currentScene);
            }
            currentScene = scene;
            //호출된 Scene의 패키지경로명 기록
            currentScenePkgPath=classPath;

            rootScene.add(scene);
            scene.onDataReceived(args);
            scene.setVisible(true);
            scene.onShow();
            //scene.requestFocus();
            rootScene.repaint();


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * clearScene - 현재 활성화된 Scene들을 종료하고 지정된 Scene으로 이동 함, 기존 SceneStack은 제거
     * @param classPath
     * @param args
     */
    public void clearScene(final String classPath, Object[] args) {
        LOG.print(this, "Set First Scene to "+classPath);
        if (rootScene == null) throw new RuntimeException("Root Scene is null");
        rootScene.remove(currentScene);
        Iterator it = this.sceneStack.iterator();
        while(it.hasNext()) {
            Scene s = (Scene) it.next();
            s._onDestroy();
            s=null;
        }
        this.sceneStack.clear();
        this.scenePkgNameStack.clear();
        currentScenePkgPath=null;
        this.pushScene(classPath, args);
    }


    /**
     * 현재 활성화 된 Scene 제거하고 이전 Scene으로 이동 함
     */
    public void popScene() {
        if(currentScene != null) {
            currentScene.setVisible(false);
            currentScene.onHide();
            rootScene.remove(currentScene);
            //GC에 의해 정리되어 언제 소멸될지 모르지만 일단 실행
            currentScene._onDestroy();
            if (sceneStack.size() >=1) {
                currentScene = (Scene)sceneStack.pop();
                currentScenePkgPath= (String) scenePkgNameStack.pop();
                rootScene.add(currentScene);
                currentScene.setVisible(true);
                //currentScene.requestFocus();
                currentScene.onShow();
                currentScene.repaint();
                rootScene.repaint();
            }
        } else {
            throw new RuntimeException("No Scene in Stack");
        }

    }

    /**
     * 전환 된 Scene의 갯수 반환
     * @return Scene 갯수
     */
    public int getCount() {
        return sceneStack.size();
    }

    /**
     * 현재 활성화된 Scene 반환
     * @return Scene
     * @see Scene
     */
    public Scene getCurrentScene() {return this.currentScene;}

    /**
     * 현재 활성화된 Scene 패키지명 반환
     * @return String
     */
    public String getCurrentScenePkgName() {return this.currentScenePkgPath;}
}
