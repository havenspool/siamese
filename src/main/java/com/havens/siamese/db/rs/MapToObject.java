
package com.havens.siamese.db.rs;

import java.util.Map;

/**
 *
 * for data base translate Map to Object
 *
 * Created by havens on 15-8-12.
 */
public interface MapToObject<E> {

    E toObject(Map<String, Object> map);

}
