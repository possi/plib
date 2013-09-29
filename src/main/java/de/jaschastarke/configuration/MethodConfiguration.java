package de.jaschastarke.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.jaschastarke.configuration.annotations.IsConfigurationNode;
import de.jaschastarke.utils.ClassDescriptorStorage;
import de.jaschastarke.utils.DocComment;
import de.jaschastarke.utils.ClassDescriptorStorage.ClassDescription;

public abstract class MethodConfiguration implements IConfiguration {
    private static final Comparator<IBaseConfigurationNode> SORTER = new Comparator<IBaseConfigurationNode>() {
        @Override
        public int compare(final IBaseConfigurationNode arg0, final IBaseConfigurationNode arg1) {
            return new Integer(arg0.getOrder()).compareTo(new Integer(arg1.getOrder()));
        }
    };
    
    protected List<IBaseConfigurationNode> nodes = new ArrayList<IBaseConfigurationNode>();

    private ClassDescriptorStorage cds = null;
    public MethodConfiguration() {
        initializeConfigNodes();
    }
    public MethodConfiguration(final ClassDescriptorStorage cds) {
        this.cds = cds;
        initializeConfigNodes();
    }
    
    private void initializeConfigNodes() {
        ClassDescription cd = null;
        if (cds != null)
            cd = cds.getClassFor(this);
        for (Method method : this.getClass().getMethods()) {
            IsConfigurationNode annot = method.getAnnotation(IsConfigurationNode.class);
            if (annot != null) {
                MethodConfigurationNode node = new MethodConfigurationNode(method, annot);
                if (cd != null)
                    node.setDescription(cd.getElDocComment(method.getName()));
                nodes.add(node);
            }
        }
        this.sort();
        
        DocComment latestdc = null;
        for (IBaseConfigurationNode node : nodes) {
            MethodConfigurationNode mn = (MethodConfigurationNode) node;
            if (mn.getStyle() == ConfigurationStyle.GROUPED_PREVIOUS) {
                if (latestdc != null && mn.getDescription() == null) {
                    mn.setDescription(latestdc);
                }
            } else {
                latestdc = cd.getElDocComment(mn.getMethod().getName());
            }
        }
    }
    public void sort() {
        if (nodes.size() > 1) {
            Collections.sort(nodes, SORTER);
        }
    }

    @Override
    public List<IBaseConfigurationNode> getConfigNodes() {
        return nodes;
    }
    
    protected Object handleGetOtherConfigurationNode(final IConfigurationNode node) {
        throw new IllegalArgumentException("This Configuration-Node isn't set for this Configuration");
    }

    @Override
    public Object getValue(final IConfigurationNode node) {
        if (node instanceof MethodConfigurationNode) {
            MethodConfigurationNode mnode = (MethodConfigurationNode) node;
            try {
                return mnode.getMethod().invoke(this);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            return handleGetOtherConfigurationNode(node);
        }
    }

}
