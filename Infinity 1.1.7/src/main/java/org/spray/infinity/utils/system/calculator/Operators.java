package org.spray.infinity.utils.system.calculator;

import java.util.function.DoubleBinaryOperator;

/**
 * 
 * @author RicoBrase
 *
 */
public enum Operators {

	PLUS(1, false, (l, r) -> l + r), MINUS(1, false, (l, r) -> l - r), MULTIPLY(2, false, (l, r) -> l * r),
	DIVIDE(2, false, (l, r) -> l / r), POW(3, true, Math::pow);

	private final int priority;
	private final boolean rightAssociative;
	private final DoubleBinaryOperator binaryOperator;

	Operators(final int priority, final boolean rightAssociative, final DoubleBinaryOperator binaryOperator) {
		this.priority = priority;
		this.rightAssociative = rightAssociative;
		this.binaryOperator = binaryOperator;
	}

	public double compute(final double left, final double right) {
		return binaryOperator.applyAsDouble(left, right);
	}

	public int getPriority() {
		return this.priority;
	}

	public boolean isRightAssociative() {
		return this.rightAssociative;
	}

}