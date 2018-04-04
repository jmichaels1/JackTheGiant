package com.escoladeltreball.dam2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import scenes.Gameplay;


public class GameMain extends Game {
	private SpriteBatch batch;
//	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
//		img = new Texture("Backgrounds/Game BG.png");
		setScreen(new Gameplay(this));
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		super.render();
	}

	public SpriteBatch getBatch(){
		return this.batch;
	}
}
