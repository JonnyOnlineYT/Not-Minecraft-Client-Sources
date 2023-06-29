package org.spray.heaven.ui.avx.view;

import org.spray.heaven.ui.avx.AVXContext;
import org.spray.heaven.ui.avx.listeners.OnClickListener;

public class ViewHeader {

	protected AVXContext context = AVXContext.getInstance();

	protected float x, y, width, height;
	protected float pointX, pointY;
	protected OnClickListener onClickListener;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getPointX() {
		return pointX;
	}

	public float getPointY() {
		return pointY;
	}

	/**
	 * Used only for internal panel elements
	 * @param x - Internal 'x' position
	 * @param y - Internal 'y' position
	 * @param width
	 * @param height
	 */
	public void setPoints(float x, float y, float width, float height) {
		this.pointX = x;
		this.pointY = y;
		this.width = width;
		this.height = height;
	}

	public void setDimension(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener listener) {
		this.onClickListener = listener;
	}
}
