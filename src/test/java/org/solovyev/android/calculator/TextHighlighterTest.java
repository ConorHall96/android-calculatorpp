/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 * or visit http://se.solovyev.org
 */

package org.solovyev.android.calculator;

import jscl.JsclMathEngine;
import jscl.MathEngine;
import jscl.NumeralBase;
import junit.framework.Assert;
import org.junit.Test;
import org.solovyev.android.calculator.model.CalculatorEngine;
import org.solovyev.android.calculator.model.TextProcessor;

import java.util.Date;
import java.util.Random;

/**
 * User: serso
 * Date: 10/12/11
 * Time: 10:07 PM
 */
public class TextHighlighterTest {

	@Test
	public void testProcess() throws Exception {
		TextProcessor<?, String> textHighlighter = new TextHighlighter(0, false, JsclMathEngine.instance);

		final Random random = new Random(new Date().getTime());
		for (int i = 0; i < 1000; i++) {
			final StringBuilder sb = new StringBuilder();
			for (int j = 0; j < 1000; j++) {
				sb.append(random.nextBoolean() ? "(" : ")");
			}
			try {
				textHighlighter.process(sb.toString());
			} catch (Exception e) {
				System.out.println(sb.toString());
				throw e;
			}
		}

		Assert.assertEquals("<font color=\"#000000\"></font>)(((())())", textHighlighter.process(")(((())())").toString());
		Assert.assertEquals(")", textHighlighter.process(")").toString());
		Assert.assertEquals(")()(", textHighlighter.process(")()(").toString());
		Assert.assertEquals("1 000 000", textHighlighter.process("1000000").toString());
		Assert.assertEquals("1 000 000", textHighlighter.process("1000000").toString());

		textHighlighter = new TextHighlighter(0, true, JsclMathEngine.instance);
		Assert.assertEquals("0.1E3", textHighlighter.process("0.1E3").toString());
		Assert.assertEquals("1E3", textHighlighter.process("1E3").toString());
		Assert.assertEquals("2<b>0x:</b>", textHighlighter.process("20x:").toString());
		Assert.assertEquals("20x", textHighlighter.process("20x").toString());
		Assert.assertEquals("22x", textHighlighter.process("22x").toString());
		Assert.assertEquals("20t", textHighlighter.process("20t").toString());
		Assert.assertEquals("20k", textHighlighter.process("20k").toString());
		Assert.assertEquals("1 000 000E3", textHighlighter.process("1000000E3").toString());
		Assert.assertEquals("-1 000 000E3", textHighlighter.process("-1000000E3").toString());
		Assert.assertEquals("-1 000 000E-3", textHighlighter.process("-1000000E-3").toString());
		Assert.assertEquals("-1 000 000E-30000", textHighlighter.process("-1000000E-30000").toString());
		textHighlighter = new TextHighlighter(0, false, JsclMathEngine.instance);

		textHighlighter.process("cannot calculate 3^10^10 !!!\n" +
				"        unable to enter 0. FIXED\n" +
				"        empty display in Xperia Rayo\n" +
				"        check привиденная FIXED\n" +
				"        set display result only if text in editor was not changed FIXED\n" +
				"        shift M text to the left\n" +
				"        do not show SYNTAX ERROR always (may be show send clock?q) FIXED\n" +
				"        ln(8)*log(8) =>  ln(8)*og(8) FIXED\n" +
				"        copy/paste ln(8)*log(8)\n" +
				"        6!^2 ERROR");

		Assert.assertEquals("<font color=\"#000000\"><i>sin</i>(</font><font color=\"#ffff9a\">2</font><font color=\"#000000\">)</font>", textHighlighter.process("sin(2)").toString());
		Assert.assertEquals("<font color=\"#000000\"><i>atanh</i>(</font><font color=\"#ffff9a\">2</font><font color=\"#000000\">)</font>", textHighlighter.process("atanh(2)").toString());


		Assert.assertEquals("<b>0x:</b>E", textHighlighter.process("0x:E").toString());
		Assert.assertEquals("<b>0x:</b>6F", textHighlighter.process("0x:6F").toString());
		Assert.assertEquals("<b>0x:</b>6F", textHighlighter.process("0x:6F.").toString());
		Assert.assertEquals("<b>0x:</b>6F", textHighlighter.process("0x:6F.2").toString());
		Assert.assertEquals("<b>0x:</b>6F", textHighlighter.process("0x:6F.B").toString());
		Assert.assertEquals("<b>0x:</b>6F", textHighlighter.process("0x:006F.B").toString());
		Assert.assertEquals("<b>0x:</b>0", textHighlighter.process("0x:0").toString());
		Assert.assertEquals("<b>0x:</b>FF 33 23 3F FE", textHighlighter.process("0x:FF33233FFE").toString());
		Assert.assertEquals("<b>0x:</b>FF 33 23 3F FE", textHighlighter.process("0x:FF33 233 FFE").toString());

		final MathEngine me = CalculatorEngine.instance.getEngine();
		try {
			me.setNumeralBase(NumeralBase.hex);
			Assert.assertEquals("E", textHighlighter.process("E").toString());
			Assert.assertEquals(".E", textHighlighter.process(".E").toString());
			Assert.assertEquals("E", textHighlighter.process("E.").toString());
			Assert.assertEquals(".E.", textHighlighter.process(".E.").toString());
			Assert.assertEquals("6F", textHighlighter.process("6F").toString());
			Assert.assertEquals("6F", textHighlighter.process("6F").toString());
			Assert.assertEquals("6F", textHighlighter.process("6F.").toString());
			Assert.assertEquals("6F", textHighlighter.process("6F.2").toString());
			Assert.assertEquals("6F", textHighlighter.process("6F.B").toString());
			Assert.assertEquals("6F", textHighlighter.process("006F.B").toString());
		} finally {
			me.setNumeralBase(NumeralBase.dec);
		}

		Assert.assertEquals("<b>0b:</b>11 0101", textHighlighter.process("0b:110101").toString());
		Assert.assertEquals("<b>0b:</b>11 0101", textHighlighter.process("0b:110101.").toString());
		Assert.assertEquals("<b>0b:</b>11 0101", textHighlighter.process("0b:110101.101").toString());
		Assert.assertEquals("<b>0b:</b>1101 0100", textHighlighter.process("0b:11010100.1").toString());
		Assert.assertEquals("<b>0b:</b>11 0101", textHighlighter.process("0b:110101.0").toString());
		Assert.assertEquals("<b>0b:</b>0", textHighlighter.process("0b:0").toString());
		Assert.assertEquals("<b>0b:</b>10 1010 0101 1110 1010 1001", textHighlighter.process("0b:1010100101111010101001").toString());
		Assert.assertEquals("<b>0b:</b>10 1010 0101 1110 1010 1001", textHighlighter.process("0b:101 010   01 0 111   1 0 10101001").toString());

		try {
			me.setNumeralBase(NumeralBase.bin);
		Assert.assertEquals("11 0101", textHighlighter.process("110101").toString());
		Assert.assertEquals("11 0101", textHighlighter.process("110101.").toString());
		Assert.assertEquals("11 0101", textHighlighter.process("110101.101").toString());
		Assert.assertEquals("1101 0100", textHighlighter.process("11010100.1").toString());
		Assert.assertEquals("11 0101", textHighlighter.process("110101.0").toString());
		Assert.assertEquals("0", textHighlighter.process("0").toString());
		Assert.assertEquals("10 1010 0101 1110 1010 1001", textHighlighter.process("1010100101111010101001").toString());
		Assert.assertEquals("10 1010 0101 1110 1010 1001", textHighlighter.process("101 010   01 0 111   1 0 10101001").toString());
		} finally {
			me.setNumeralBase(NumeralBase.dec);
		}
	}
}
