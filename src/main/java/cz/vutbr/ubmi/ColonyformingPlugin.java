/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package cz.vutbr.ubmi;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.miginfocom.swing.MigLayout;

import org.scijava.Context;
import org.scijava.ItemVisibility;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import ij.IJ;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;





/**
 * This example illustrates how to create an ImageJ {@link Command} plugin.
 * <p>
 * The code here is a simple Gaussian blur using ImageJ Ops.
 * </p>
 * <p>
 * You should replace the parameter fields with your own inputs and outputs,
 * and replace the {@link run} method implementation with your own logic.
 * </p>
 */
@Plugin(type = Command.class, menuPath = "Plugins>ColonyformingPlugin")
public class ColonyformingPlugin<T extends RealType<T>> implements Command {


	@Parameter
	private Dataset currentData;
	

	@Parameter
	private UIService uiService;

	@Parameter
	public OpService opService;

	
	JFrame  frame;

	ColonyView view;
	ColonyModel model;
	ColonyController controller;

	@Override
	public void run() {
		
		
		RandomAccessibleInterval<T> img = (RandomAccessibleInterval<T> ) currentData.getImgPlus();
		
		this.model= new ColonyModel(img);
		this.view = new ColonyView(model,opService);
		this.controller = new ColonyController(model,view);
		

	}

	/**
	 * This main function serves for development purposes.
	 * It allows you to run the plugin immediately out of
	 * your integrated development environment (IDE).
	 *
	 * @param args whatever, it's ignored
	 * @throws Exception
	 */
	public static void main(final String... args) throws Exception {

		final ImageJ ij = new ImageJ();
		ij.ui().showUI();
		IJ.openImage( "res/test.tif" ).show();
		ij.command().run(ColonyformingPlugin.class, true);

	}



}
