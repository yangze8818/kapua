/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model.config.metatype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Java class for Tocd complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Tocd"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AD" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Tad" maxOccurs="unbounded"/&gt;
 *         &lt;element name="Icon" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Ticon" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @since 1.0
 */
@XmlRootElement(name = "OCD", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = MetatypeXmlRegistry.class, factoryMethod = "newKapuaTocd")
public interface KapuaTocd {

    /**
     * Gets the value of the ad property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ad property.
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getAD().add(newItem);
     * </pre>
     */
    @XmlElement(name = "AD", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0", required = true)
    List<KapuaTad> getAD();

    void setAD(List<? extends KapuaTad> icon);

    /**
     * Gets the value of the icon property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the icon property.
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getIcon().add(newItem);
     * </pre>
     */
    @XmlElement(name = "Icon", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
    List<KapuaTicon> getIcon();

    void setIcon(List<? extends KapuaTicon> icon);

    /**
     * Gets the value of the any property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getAny().add(newItem);
     * </pre>
     */
    @XmlAnyElement(lax = true)
    List<Object> getAny();

    void setAny(List<Object> any);

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "name", required = true)
    String getName();

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     */
    void setName(String value);

    /**
     * Gets the value of the description property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "description")
    String getDescription();

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is {@link String }
     */
    void setDescription(String value);

    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "id", required = true)
    String getId();

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is {@link String }
     */
    void setId(String value);

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * <p>
     * the map is keyed by the name of the attribute and
     * the value is the string value of the attribute.
     * <p>
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    @XmlAnyAttribute
    Map<QName, String> getOtherAttributes();

    void setOtherAttributes(Map<QName, String> otherAttributes);

}
