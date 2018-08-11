package org.foa.data.optiondata;

import org.foa.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 王川源
 */
public interface OptionDAO extends JpaRepository<Option, String>, OptionCustom{
}
