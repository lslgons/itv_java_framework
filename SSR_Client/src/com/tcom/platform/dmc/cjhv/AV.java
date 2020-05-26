package com.tcom.platform.dmc.cjhv;

import java.awt.Rectangle;

import com.tcom.platform.dmc.interfaces.AVInterface;
import com.tcom.platform.dmc.interfaces.VODInterface;
import com.tcom.util.LOG;

public class AV implements AVInterface, VODInterface{

	private int fx, fy, fw, fh;
	private java.awt.Rectangle fullRec, curRec;

	public AV() {
		javax.media.Player player = null;
		javax.tv.service.selection.ServiceContentHandler[] handlers  = null;
		javax.tv.media.AWTVideoSizeControl sizeCntr = null;
		java.awt.Rectangle curRect = null;

		// http://www.interactivetvweb.org/tutorials/ocap/jmf

		handlers  = javax.tv.service.selection.ServiceContextFactory.getInstance().getServiceContexts()[0].getServiceContentHandlers();
		LOG.print(this, "resizeAV - Rectangle : handlers.length " + handlers.length );
		for(int i=0; i < handlers.length ; i++) {
			if (handlers[i] instanceof javax.tv.service.selection.ServiceMediaHandler) {
				player = (javax.media.Player) handlers[i];
				LOG.print(this, "resizeAV - Rectangle : handlers["+i+ "] "+ handlers[i] );
			}
		}

		if(player == null){
			LOG.print(this, "resizeAV - Rectangle : player = null " + (player == null) );
			return;
		}

		//set the video size  switching full screen & pip screen
		sizeCntr = ( javax.tv.media.AWTVideoSizeControl ) player.getControl( "javax.tv.media.AWTVideoSizeControl" );
		curRect = new java.awt.Rectangle( sizeCntr.getSourceVideoSize() ); // 소스 비디오 사이즈

		this.fx = 0;
		this.fy = 0;
		this.fw = curRect.width;
		this.fh = curRect.height;

		LOG.print(this, "============================================");
		LOG.print(this, "Current Rect : "+ curRect.width+", "+curRect.height);
		LOG.print(this, "============================================");



		this.fullRec = new java.awt.Rectangle(this.fx, this.fy, this.fw, this.fh);
		this.curRec = null;
	}


	public void resetVideoSize() {
		changeVideoSize(fullRec);
	}

	public void changeVideoSize(Rectangle rect) {
		//CJHV는 960X540기준 사이즈 조절하므로 변환X
		LOG.print(this, "resizeAV - to : " + rect);
//		if (this.curMode == sizeMode) return;
		this.curRec=rect;


		javax.tv.service.selection.ServiceContext sc = null;
		javax.media.Player player = null;
		javax.tv.service.selection.ServiceContentHandler[] handlers  = null;
		javax.tv.media.AWTVideoSizeControl sizeCntr = null;
		java.awt.Rectangle NewBound = null;
		java.awt.Rectangle rec = null;
		javax.tv.media.AWTVideoSize VideoSize = null;
		javax.tv.media.AWTVideoSize CheckedSize = null;
		javax.tv.media.AWTVideoSize CurBound = null;
		try {

			sc = javax.tv.service.selection.ServiceContextFactory.getInstance().getServiceContexts()[0];

			// http://www.interactivetvweb.org/tutorials/ocap/jmf

			handlers  = sc.getServiceContentHandlers();
			LOG.print(this, "resizeAV - Rectangle : handlers.length " + handlers.length );
			for(int i=0; i < handlers.length ; i++) {
				if (handlers[i] instanceof javax.tv.service.selection.ServiceMediaHandler) {
					player = (javax.media.Player) handlers[i];
					LOG.print(this, "resizeAV - Rectangle : handlers["+i+ "] "+ handlers[i] );
				}
			}

			if(player == null){
				LOG.print(this, "resizeAV - Rectangle : player = null " + (player == null) );
				return;
			}


			sizeCntr = ( javax.tv.media.AWTVideoSizeControl ) player.getControl( "javax.tv.media.AWTVideoSizeControl" );
			NewBound = new java.awt.Rectangle( sizeCntr.getSourceVideoSize() ); // 소스 비디오 사이즈
			VideoSize = new javax.tv.media.AWTVideoSize( NewBound,  this.curRec); // 소스 비디오 사이즈를 풀 사이즈로 맞추려면 몇배로 늘려야하는가

//			Rs.print(this, "resizeAV - rec(curMode) width : " + rec.width + " / rec height : " + rec.height); // 풀 사이즈
//			Rs.print(this, "resizeAV - NewBound(SourceVideoSize) width : " + NewBound.width + " / NewBound height : " + NewBound.height); // 소스 비디오 사이즈
//			Rs.print(this, "resizeAV - VideoSize.getXScale : " + VideoSize.getXScale() + " / VideoSize.getYScale : " + VideoSize.getYScale()); // 소스 비디오 사이즈를 풀 사이즈로 맞추려면 몇배로 늘려야하는가
//			Rs.print(this, "resizeAV - VideoSize.width : " + VideoSize.getDestination().width + " / VideoSize.height : " + VideoSize.getDestination().height);

			if (curRec.width != 0 && curRec.height != 0) {
				LOG.print(this, "resizeAV -  : 1- 0 " );
				CheckedSize = sizeCntr.checkSize( VideoSize ); // 하드웨어가 지원하는 가장 근접한 사이즈 반환 VideoSize -> CheckedSize
				CurBound = sizeCntr.getSize(); // 현재 사이즈일거 같고
//				Rs.print(this, "resizeAV - CurBound width : " + CurBound.getDestination().width + " / CurBound height : " + CurBound.getDestination().height );
//				Rs.print(this, "resizeAV - CheckedSize width : " + CheckedSize.getDestination().width + " / CheckedSize height : " + CheckedSize.getDestination().height );
//				Rs.print(this, "resizeAV - CurBound.getXScale() : " + CurBound.getXScale() + " / CurBound.getYScale() : " + CurBound.getYScale() );
//				Rs.print(this, "resizeAV - CheckedSize.getXScale() : " + CheckedSize.getXScale() + " / CheckedSize.getYScale() : " + CheckedSize.getYScale() );
//				Rs.print(this, "resizeAV - !CurBound.equals( CheckedSize ) : " + !CurBound.equals( CheckedSize ));

				if ( !CurBound.equals( CheckedSize ) ) {
					LOG.print(this, "resizeAV -  : 1- 1 " );
					sizeCntr.setSize( CheckedSize );
				}
			} else {
				LOG.print(this, "resizeAV -  : 2- 0 " );
				sizeCntr.setSize( VideoSize );
			}

		} catch (Exception e) {
			LOG.print(this, "resizeAV Error :: " + e.getMessage());
		} finally {
			sc = null;
			player = null;
			if(handlers  != null){
				for(int i=0; i < handlers.length ; i++) {
					handlers[i] = null;
				}
				handlers  = null;
			}
			sizeCntr = null;
			NewBound = null;
			rec = null;
			VideoSize = null;
			CheckedSize = null;
			CurBound = null;
		}
		
	}

	public void turnOnVideo() {
		LOG.print(this, "Not Implemented");
	}

	public void turnOffVideo() {
		LOG.print(this, "Not Impleented");
	}

	public Rectangle getCurrentVideoSize() {
		return this.curRec;
	}

	public void startVOD(String assetId) {
		// TODO Auto-generated method stub
		
	}

	public void destroyVOD() {
		// TODO Auto-generated method stub
		
	}

	public void vodKeyHandler(int keyCode) {
		// TODO Auto-generated method stub
		
	}

	public int getTotPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getTotLongPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCutPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getCurLongPlayTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPalyStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addVodEventListener(VODEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void removeVodEventListener(VODEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void onlyVODStop() {
		// TODO Auto-generated method stub
		
	}

	public void forcedVODPause() {
		// TODO Auto-generated method stub
		
	}

}
