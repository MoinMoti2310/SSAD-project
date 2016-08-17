package ourfood;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

/**
 * Class to enable seemless integration with CDN
 * 
 \* @author raghu.mulukoju
 *
 */
@Component
public class StaticDialect extends AbstractDialect {
    /**
     * Location of the static files
     */
    @Value("${static.url}")
    String url;

    /*
     * Default prefix: this is the prefix that will be used for this dialect unless a different one is specified when
     * adding the dialect to the Template Engine.
     */
    public String getPrefix() {
        return "static";
    }

    /*
     * Two attribute processors are declared: 'classforposition' and 'remarkforposition'. Also one element processor:
     * the 'headlines' tag.
     */
    // TODO see if both the processors can be clubbed into one
    @Override
    public Set<IProcessor> getProcessors() {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new StaticHrefProcessor(url));
        processors.add(new StaticSrcProcessor(url));
        return processors;
    }
}