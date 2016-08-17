package ourfood;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public class StaticSrcProcessor extends AbstractAttributeModifierAttrProcessor {

    /**
     * Location of the static files
     */
    private String url;

    public StaticSrcProcessor(String url) {
        super("src");
        this.url = url;
    }

    public int getPrecedence() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected Map<String, String> getModifiedAttributeValues(final Arguments arguments, final Element element,
            final String attributeName) {
        final Configuration configuration = arguments.getConfiguration();
        final String attributeValue = element.getAttributeValue(attributeName);
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(configuration, arguments, attributeValue);
        final String resource = (String) expression.execute(configuration, arguments);
        final Map<String, String> values = new HashMap<String, String>();
        if (url != null) {
            values.put("src", url + resource);
        } else {
            values.put("src", resource);
        }
        return values;
    }

    @Override
    protected ModificationType getModificationType(final Arguments arguments, final Element element,
            final String attributeName, final String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }

    @Override
    protected boolean removeAttributeIfEmpty(final Arguments arguments, final Element element,
            final String attributeName, final String newAttributeName) {
        return true;
    }

    @Override
    protected boolean recomputeProcessorsAfterExecution(final Arguments arguments, final Element element,
            final String attributeName) {
        return false;
    }
}