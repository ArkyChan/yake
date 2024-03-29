/*
 * DummyRenderer.java
 * Copyright (C) 2003
 *
 * $Id: DummyRenderer.java,v 1.2 2005/02/07 22:37:55 cawe Exp $
 */
 
package yayGame.yake.render;

import java.awt.Dimension;
import java.awt.DisplayMode;

import yayGame.yake.client.refdef_t;
import yayGame.yake.client.refexport_t;
import yayGame.yake.qcommon.xcommand_t;
import yayGame.yake.sys.KBD;

/**
 * DummyRenderer
 * 
 * @author cwei
 */
public class DummyRenderer implements refexport_t {

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#Init(int, int)
	 */
	public boolean Init(int vid_xpos, int vid_ypos) {
		return false;
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#Shutdown()
	 */
	public void Shutdown() {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#BeginRegistration(java.lang.String)
	 */
	public void BeginRegistration(String map) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#RegisterModel(java.lang.String)
	 */
	public model_t RegisterModel(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#RegisterSkin(java.lang.String)
	 */
	public image_t RegisterSkin(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#RegisterPic(java.lang.String)
	 */
	public image_t RegisterPic(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#SetSky(java.lang.String, float, float[])
	 */
	public void SetSky(String name, float rotate, float[] axis) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#EndRegistration()
	 */
	public void EndRegistration() {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#RenderFrame(yayGame.yake.client.refdef_t)
	 */
	public void RenderFrame(refdef_t fd) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawGetPicSize(java.awt.Dimension, java.lang.String)
	 */
	public void DrawGetPicSize(Dimension dim, String name) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawPic(int, int, java.lang.String)
	 */
	public void DrawPic(int x, int y, String name) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawStretchPic(int, int, int, int, java.lang.String)
	 */
	public void DrawStretchPic(int x, int y, int w, int h, String name) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawChar(int, int, int)
	 */
	public void DrawChar(int x, int y, int num) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawTileClear(int, int, int, int, java.lang.String)
	 */
	public void DrawTileClear(int x, int y, int w, int h, String name) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawFill(int, int, int, int, int)
	 */
	public void DrawFill(int x, int y, int w, int h, int c) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawFadeScreen()
	 */
	public void DrawFadeScreen() {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#DrawStretchRaw(int, int, int, int, int, int, byte[])
	 */
	public void DrawStretchRaw(int x, int y, int w, int h, int cols, int rows, byte[] data) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#CinematicSetPalette(byte[])
	 */
	public void CinematicSetPalette(byte[] palette) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#BeginFrame(float)
	 */
	public void BeginFrame(float camera_separation) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#EndFrame()
	 */
	public void EndFrame() {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#AppActivate(boolean)
	 */
	public void AppActivate(boolean activate) {
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#updateScreen(yayGame.yake.qcommon.xcommand_t)
	 */
	public void updateScreen(xcommand_t callback) {
	    callback.execute();
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#apiVersion()
	 */
	public int apiVersion() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#getModeList()
	 */
	public DisplayMode[] getModeList() {
		return null;
	}

	/* (non-Javadoc)
	 * @see yayGame.yake.client.refexport_t#getKeyboardHandler()
	 */
	public KBD getKeyboardHandler() {
		return null;
	}

}
