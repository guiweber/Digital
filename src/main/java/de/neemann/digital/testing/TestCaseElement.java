package de.neemann.digital.testing;

import de.neemann.digital.core.Model;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValues;
import de.neemann.digital.core.element.*;

/**
 * Dummy to represent the testdata in the circuit.
 *
 * @author hneemann
 */
public class TestCaseElement implements Element {

    /**
     * the used {@link ElementAttributes} key
     */
    public static final Key<TestCaseDescription> TESTDATA = new Key<>("Testdata", TestCaseDescription.DEFAULT);

    /**
     * The TestCaseElement description
     */
    public static final ElementTypeDescription TESTCASEDESCRIPTION
            = new ElementTypeDescription("Testcase", TestCaseElement.class)
            .addAttribute(Keys.LABEL)
            .addAttribute(TESTDATA);

    /**
     * creates a new instance
     *
     * @param attributes the attributes
     */
    public TestCaseElement(ElementAttributes attributes) {
    }

    @Override
    public void setInputs(ObservableValues inputs) throws NodeException {
    }

    @Override
    public ObservableValues getOutputs() {
        return ObservableValues.EMPTY_LIST;
    }

    @Override
    public void registerNodes(Model model) {
    }
}
