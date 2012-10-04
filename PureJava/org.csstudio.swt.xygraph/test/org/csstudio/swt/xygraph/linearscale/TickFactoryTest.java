package org.csstudio.swt.xygraph.linearscale;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class TickFactoryTest {

	private static final double ERROR = 1e-9;

	@Test
	public void testRounding() {
		try {
			TickFactory.roundDown(BigDecimal.ONE, BigDecimal.ZERO);
		} catch (IllegalArgumentException iae) {
			
		} catch (Exception e) {
			Assert.fail("Did not throw IAE");
		}

		try {
			TickFactory.roundUp(BigDecimal.ONE, BigDecimal.ZERO);
		} catch (IllegalArgumentException iae) {
			
		} catch (Exception e) {
			Assert.fail("Did not throw IAE");
		}

		Assert.assertEquals(0, TickFactory.roundDown(BigDecimal.ZERO, BigDecimal.ONE), ERROR);
		Assert.assertEquals(0, TickFactory.roundUp(BigDecimal.ZERO, BigDecimal.ONE), ERROR);

		Assert.assertEquals(1, TickFactory.roundDown(BigDecimal.valueOf(1.5), BigDecimal.ONE), ERROR);
		Assert.assertEquals(2, TickFactory.roundUp(BigDecimal.valueOf(1.5), BigDecimal.ONE), ERROR);

		Assert.assertEquals(1, TickFactory.roundDown(BigDecimal.valueOf(1), BigDecimal.ONE), ERROR);
		Assert.assertEquals(1, TickFactory.roundUp(BigDecimal.valueOf(1), BigDecimal.ONE), ERROR);

		Assert.assertEquals(0, TickFactory.roundDown(BigDecimal.valueOf(0.5), BigDecimal.ONE), ERROR);
		Assert.assertEquals(1, TickFactory.roundUp(BigDecimal.valueOf(0.5), BigDecimal.ONE), ERROR);

		Assert.assertEquals(-1, TickFactory.roundDown(BigDecimal.valueOf(-0.5), BigDecimal.ONE), ERROR);
		Assert.assertEquals(0, TickFactory.roundUp(BigDecimal.valueOf(-0.5), BigDecimal.ONE), ERROR);

		Assert.assertEquals(-1, TickFactory.roundDown(BigDecimal.valueOf(-1), BigDecimal.ONE), ERROR);
		Assert.assertEquals(-1, TickFactory.roundUp(BigDecimal.valueOf(-1), BigDecimal.ONE), ERROR);

		Assert.assertEquals(-2, TickFactory.roundDown(BigDecimal.valueOf(-1.5), BigDecimal.ONE), ERROR);
		Assert.assertEquals(-1, TickFactory.roundUp(BigDecimal.valueOf(-1.5), BigDecimal.ONE), ERROR);

		Assert.assertEquals(-1.6, TickFactory.roundDown(BigDecimal.valueOf(-1.5), BigDecimal.valueOf(0.2)), ERROR);
		Assert.assertEquals(-1.4, TickFactory.roundUp(BigDecimal.valueOf(-1.5), BigDecimal.valueOf(0.2)), ERROR);

		Assert.assertEquals(-0.2, TickFactory.roundDown(BigDecimal.valueOf(-0.1), BigDecimal.valueOf(0.2)), ERROR);
		Assert.assertEquals(0, TickFactory.roundUp(BigDecimal.valueOf(-0.1), BigDecimal.valueOf(0.2)), ERROR);

		Assert.assertEquals(-0.2, TickFactory.roundDown(BigDecimal.valueOf(-0.2), BigDecimal.valueOf(0.2)), ERROR);
		Assert.assertEquals(-0.2, TickFactory.roundUp(BigDecimal.valueOf(-0.2), BigDecimal.valueOf(0.2)), ERROR);

		Assert.assertEquals(0, TickFactory.roundDown(BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.2)), ERROR);
		Assert.assertEquals(0.2, TickFactory.roundUp(BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.2)), ERROR);

		Assert.assertEquals(0.2, TickFactory.roundDown(BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.2)), ERROR);
		Assert.assertEquals(0.2, TickFactory.roundUp(BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.2)), ERROR);

		Assert.assertEquals(0.6, TickFactory.roundDown(BigDecimal.valueOf(0.7), BigDecimal.valueOf(0.2)), ERROR);
		Assert.assertEquals(0.8, TickFactory.roundUp(BigDecimal.valueOf(0.7), BigDecimal.valueOf(0.2)), ERROR);
	}
}
