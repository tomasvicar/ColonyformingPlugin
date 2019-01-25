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
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import ij.IJ;

import java.util.ArrayList;
import java.util.List;





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
    private OpService opService;

    @Override
    public void run() {
        final Img<T> image = (Img<T>)currentData.getImgPlus();

        
        final double[] sigmas = {1.0};

        List<RandomAccessibleInterval<T>> results = new ArrayList<>();

        for (double sigma : sigmas) {
            results.add(opService.filter().gauss(image, sigma));
        }

        
        for (RandomAccessibleInterval<T> elem : results) {
            uiService.show(elem);
        }
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
//        final Dataset dataset = ij.scifio().datasetIO().open("res/test.tif");
        ij.command().run(ColonyformingPlugin.class, true);
        
//        final Dataset dataset = ij.scifio().datasetIO().open("res/test.tif");
//        ij.ui().show(dataset);
//        ij.command().run(ColonyformingPlugin.class, true);
        
//        final File file = ij.ui().chooseFile(null, "open");
//        if (file != null) {
//            final Dataset dataset = ij.scifio().datasetIO().open(file.getPath());
//            ij.ui().show(dataset);
//            ij.command().run(ColonyformingPlugin.class, true);
//        }
        
        
        
    }

}
