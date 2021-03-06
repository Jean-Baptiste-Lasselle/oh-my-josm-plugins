// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.ImportImagePlugin;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.ExtensionFileFilter;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.io.importexport.FileImporter;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.io.IllegalDataException;
import org.openstreetmap.josm.plugins.ImportImagePlugin.ImageLayer.LayerCreationCanceledException;

/**
 * Class to open georeferenced image with standard file open dialog 
 */
public class ImportImageFileImporter extends FileImporter {
    
    private Logger logger = Logger.getLogger(LoadImageAction.class);

    public ImportImageFileImporter() {
        super(new ExtensionFileFilter("tiff,tif,jpg,jpeg,bmp,png", "jpg", 
                "Georeferenced image file [by ImportImage plugin] (*.jpg, *.jpeg, *.tif, *.tiff, *.png, *.bmp)"));
    }

    @Override
    public boolean isBatchImporter() {
        return true;
    }

    @Override
    public double getPriority() {
        return -3;
    }

    @Override
    public void importData(List<File> files, ProgressMonitor progressMonitor) throws IOException, IllegalDataException {
        if (null == files || files.isEmpty()) return;

        for (File file: files) {
            if (file.isDirectory()) continue;
            ImageLayer layer = null;
            logger.info("File choosen:" + file);
            try {
                layer = new ImageLayer(file);
            } catch (LayerCreationCanceledException e) {
                // if user decides that layer should not be created just return.
                continue;
            } catch (Exception e) {
               logger.error("Error while creating image layer: \n" + e.getMessage());
                JOptionPane.showMessageDialog(null, tr("Error while creating image layer: {0}", e.getCause()));
                continue;
            }

            // Add layer:
            MainApplication.getLayerManager().addLayer(layer);
            BoundingXYVisitor boundingXYVisitor = new BoundingXYVisitor();
            layer.visitBoundingBox(boundingXYVisitor);
            MainApplication.getMap().mapView.zoomTo(boundingXYVisitor);
        }
    }
}
