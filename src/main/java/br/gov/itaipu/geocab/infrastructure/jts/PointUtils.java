package br.gov.itaipu.geocab.infrastructure.jts;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Created by lcvmelo on 17/02/2017.
 */
public class PointUtils {

    public static String coordinateString(final Point point) {
        final WKTWriter writer = new WKTWriter();
        return writer.write(point);
    }
}
