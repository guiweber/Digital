package de.neemann.digital.gui.components.table;


import de.neemann.digital.analyse.AnalyseException;
import de.neemann.digital.analyse.ModelAnalyser;
import de.neemann.digital.analyse.TruthTable;
import de.neemann.digital.analyse.expression.Expression;
import de.neemann.digital.analyse.expression.ExpressionException;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.expression.format.FormatterException;
import de.neemann.digital.analyse.expression.modify.ExpressionModifier;
import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;
import de.neemann.digital.builder.circuit.CircuitBuilder;
import de.neemann.digital.core.BacktrackException;
import de.neemann.digital.core.Model;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.basic.*;
import de.neemann.digital.draw.elements.PinException;
import de.neemann.digital.draw.library.ElementLibrary;
import de.neemann.digital.draw.library.ElementNotFoundException;
import de.neemann.digital.draw.model.ModelCreator;
import de.neemann.digital.draw.shapes.ShapeFactory;
import junit.framework.TestCase;

import static de.neemann.digital.analyse.expression.Not.not;
import static de.neemann.digital.analyse.expression.Operation.and;
import static de.neemann.digital.analyse.expression.Operation.or;
import static de.neemann.digital.analyse.expression.Variable.v;

/**
 * Created by hneemann on 02.04.17.
 */
public class BuilderExpressionCreatorTest extends TestCase {
    private ElementLibrary libary = new ElementLibrary();
    private ShapeFactory shapeFactory = new ShapeFactory(libary);

    public void testSimple() throws FormatterException, ExpressionException, ElementNotFoundException, PinException, NodeException, AnalyseException, BacktrackException {
        Variable a = v("A");
        Variable b = v("B");
        Expression xor = or(and(a, not(b)), and(not(a), b));

        ExpressionListenerStore els = new ExpressionListenerStore(null);
        els.resultFound("xor", xor);
        els.close();

        Model m = create(els, ExpressionModifier.IDENTITY);
        assertEquals(5, m.size());
        assertEquals(2, m.findNode(And.class).size());
        assertEquals(1, m.findNode(Or.class).size());
        assertEquals(2, m.findNode(Not.class).size());
        check(m);

        m = create(els, new de.neemann.digital.analyse.expression.modify.NAnd());
        assertEquals(5, m.size());
        assertEquals(2, m.findNode(Not.class).size());
        assertEquals(3, m.findNode(NAnd.class).size());
        check(m);

        m = create(els, new de.neemann.digital.analyse.expression.modify.NOr());
        assertEquals(6, m.size());
        assertEquals(3, m.findNode(Not.class).size());
        assertEquals(3, m.findNode(NOr.class).size());
        check(m);
    }

    private void check(Model m) throws AnalyseException, NodeException, BacktrackException, PinException {
        TruthTable tt = new ModelAnalyser(m).analyse();
        assertEquals(1,tt.getResultCount());
        BoolTable r = tt.getResult(0);
        assertEquals(4, r.size());
        assertEquals(ThreeStateValue.zero ,r.get(0));
        assertEquals(ThreeStateValue.one ,r.get(1));
        assertEquals(ThreeStateValue.one ,r.get(2));
        assertEquals(ThreeStateValue.zero ,r.get(3));
    }

    private Model create(ExpressionListenerStore els, ExpressionModifier modifier) throws ExpressionException, FormatterException, ElementNotFoundException, PinException, NodeException {
        CircuitBuilder circuitBuilder = new CircuitBuilder(shapeFactory, false);
        new BuilderExpressionCreator(circuitBuilder, modifier).create(els);
        return new ModelCreator(circuitBuilder.createCircuit(), libary).createModel(false);
    }

}