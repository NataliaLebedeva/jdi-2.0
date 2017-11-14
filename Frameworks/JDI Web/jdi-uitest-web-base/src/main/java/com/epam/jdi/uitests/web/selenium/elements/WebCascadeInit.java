package com.epam.jdi.uitests.web.selenium.elements;
/*
 * Copyright 2004-2016 EPAM Systems
 *
 * This file is part of JDI project.
 *
 * JDI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JDI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JDI. If not, see <http://www.gnu.org/licenses/>.
 */


import com.epam.jdi.uitests.core.initialization.CascadeInit;
import com.epam.jdi.uitests.core.interfaces.ISetup;
import com.epam.jdi.uitests.core.interfaces.base.IBaseElement;
import com.epam.jdi.uitests.core.templates.base.Section;
import com.epam.jdi.uitests.web.selenium.driver.get.driver.DriverTypes;
import com.epam.jdi.uitests.web.selenium.elements.actions.WebActions;
import com.epam.jdi.uitests.web.selenium.elements.apiInteract.WebEngine;
import com.epam.jdi.uitests.web.selenium.elements.base.BaseElement;
import com.epam.jdi.uitests.web.selenium.elements.base.Element;
import com.epam.jdi.uitests.web.selenium.elements.base.J;
import com.epam.jdi.uitests.web.selenium.elements.complex.Elements;
import com.epam.jdi.uitests.web.selenium.elements.complex.table.EntityTable;
import com.epam.jdi.uitests.web.selenium.elements.complex.table.Table;
import com.epam.jdi.uitests.web.selenium.elements.composite.WebPage;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.FindBy;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.Frame;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.JPage;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.simple.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.epam.jdi.tools.LinqUtils.any;
import static com.epam.jdi.tools.LinqUtils.first;
import static com.epam.jdi.tools.ReflectionUtils.isClass;
import static com.epam.jdi.tools.ReflectionUtils.isInterface;
import static com.epam.jdi.tools.StringUtils.LINE_BREAK;
import static com.epam.jdi.uitests.core.initialization.MapInterfaceToElement.getClassFromInterface;
import static com.epam.jdi.uitests.core.settings.JDIData.APP_VERSION;
import static com.epam.jdi.uitests.core.settings.JDISettings.exception;
import static com.epam.jdi.uitests.web.selenium.driver.WebDriverFactory.currentDriverName;
import static com.epam.jdi.uitests.web.selenium.elements.composite.WebSite.currentSite;
import static com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.WebAnnotationsUtil.*;
import static com.epam.jdi.uitests.web.selenium.settings.WebSettings.*;

/**
 * Created by Roman_Iovlev on 6/10/2015.
 */
public class WebCascadeInit extends CascadeInit {
    @Override
    protected Class<?>[] stopTypes() { return new Class<?>[] {Object.class, WebPage.class, Section.class, Element.class}; }
    @Override
    protected Class<?>[] decorators() { return new Class<?>[] {IBaseElement.class, List.class, WebElement.class }; }

    protected void fillPageFromAnnotation(Field field, IBaseElement instance, Class<?> parentType) {
        if (field.getType().isAnnotationPresent(JPage.class))
            fillPageFromAnnotaiton((WebPage) instance, field.getType().getAnnotation(JPage.class), parentType);
        else {
            if (field.isAnnotationPresent(JPage.class))
                fillPageFromAnnotaiton((WebPage) instance, field.getAnnotation(JPage.class), parentType);
        }
    }
    protected IBaseElement fillInstance(IBaseElement instance, Field field) {
        BaseElement element = (BaseElement) instance;
        if (!element.hasLocator())
            element.setEngine(new WebEngine(element, getNewLocator(field)));
        return element;
    }
    protected IBaseElement initElements(Field field, Class<IBaseElement> genericClass) {
        return new Elements(getNewLocatorFromField(field), genericClass);
    }

    public static void initSite(Class<?> site, String driverName) {
        WebActions.Init();
        new WebCascadeInit().initStaticPages(site, driverName);
        currentSite = site;
    }
    public static <T> T initPageObject(Class<T> clazz) {
        return initPageObject(clazz, currentDriverName);
    }
    public static <T> T initPageObject(Class<T> clazz, WebDriver driver) {
        return initPageObject(clazz, useDriver(() -> driver));
    }
    public static <T> T initPageObject(Class<T> clazz, DriverTypes driver){
        return initPageObject(clazz, useDriver(driver));
    }
    public static <T> T initPageObject(Class<T> clazz, String driverName) {
        WebActions.Init();
        T page;
        try {
            page = clazz.newInstance();
        } catch (Exception ignore) {
            try {
                page = clazz.getDeclaredConstructor(WebDriver.class).newInstance(getDriver());
            } catch (Exception ex) {
                throw new RuntimeException("Can't init PageObject: " + clazz.getName() + ". Exception: " + ex.getMessage());
            }
        }
        new WebCascadeInit().initElements(page, driverName);
        return page;
    }

    @SafeVarargs
    public static <T> void initPageObject(Class<T>... clazz) {
        initPageObject(currentDriverName, clazz);
    }
    @SafeVarargs
    public static <T> void initPageObject(WebDriver driver, Class<T>... clazz) {
        initDriver();
        initPageObject(useDriver(() -> driver), clazz);
    }
    @SafeVarargs
    public static <T> void initPageObject(DriverTypes driver, Class<T>... clazz){
        initDriver();
        initPageObject(useDriver(driver), clazz);
    }
    @SafeVarargs
    public static <T> void initPageObject(String driverName, Class<T>... classes) {
        for(Class<T> clazz : classes)
            initPageObject(clazz, driverName);
    }
    @Override
    protected IBaseElement fillFromJDIAnnotation(IBaseElement instance, Field field) {
        BaseElement element = (BaseElement) instance;
        fillFromAnnotation(element, field);
        return element;
    }
    @Override
    protected IBaseElement specificAction(IBaseElement instance, Field field, Object parent, Class<?> type) {
        BaseElement element = (BaseElement) instance;
        if (parent != null && type == null)
            return element;
        By frameBy = getFrame(field.getDeclaredAnnotation(Frame.class));
        if (frameBy != null)
            element.engine().setFrame(frameBy);
        return element;
    }
    protected IBaseElement getElementsRules(Field field, String driverName, Class<?> type, String fieldName) throws IllegalAccessException, InstantiationException {
        By newLocator = getNewLocator(field);
        BaseElement instance = null;
        if (isClass(type, EntityTable.class))
            throw exception(
            "Entity table should have constructor for correct initialization." + LINE_BREAK +
                "Use following initialization: 'public EntityTable<Entity, Row> jobsListEntity = new EntityTable<>(Entity.class, Row.class);'" + LINE_BREAK +
                "Or short: 'public EntityTable<Entity, ?> simpleTable = new EntityTable<>(Entity.class)' if you have flat table");
        if (isInterface(type, List.class)) {
            Class<?> elementClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            if (isClass(elementClass, WebElement.class))
                elementClass = J.class;
            else if (elementClass.isInterface())
                elementClass = getClassFromInterface(type);
            if (elementClass != null && !isClass(elementClass, Table.class))
                instance = new Elements(newLocator, elementClass);
        }
        if (instance == null) {
            if (type.isInterface())
                type = getClassFromInterface(type);
            if (type != null) {
                instance = (BaseElement) type.newInstance();
                if (newLocator == null)
                    newLocator = instance.getLocator();
                if (instance.engine() != null && newLocator == null)
                    instance.setEngine(new WebEngine(instance));
                else
                    instance.setEngine(new WebEngine(instance, newLocator));
            }
        }
        if (instance == null)
            throw exception("Unknown interface: %s (%s). Add relation interface -> class using MapInterfaceToElement.update(...)",
                    type, fieldName);
        instance.engine().setDriverName(driverName);
        return instance;
    }

    protected By getNewLocatorFromField(Field field) {
        FindBy[] jfindbys = field.getAnnotationsByType(FindBy.class);
        if (jfindbys.length > 0 && any(jfindbys, j -> APP_VERSION.equals(j.group())))
            return findByToBy(first(jfindbys, j -> APP_VERSION.equals(j.group())));
        if (field.isAnnotationPresent(FindBy.class))
            return findByToBy(field.getAnnotation(FindBy.class));
        if (field.isAnnotationPresent(org.openqa.selenium.support.FindBy.class))
            return findByToBy(field.getAnnotation(org.openqa.selenium.support.FindBy.class));
        if (field.isAnnotationPresent(Css.class))
            return findByToBy(field.getAnnotation(Css.class));
        if (field.isAnnotationPresent(XPath.class))
            return findByToBy(field.getAnnotation(XPath.class));
        if (field.isAnnotationPresent(Text.class))
            return findByToBy(field.getAnnotation(Text.class));
        if (field.isAnnotationPresent(WithText.class))
            return findByToBy(field.getAnnotation(WithText.class));
        if (field.isAnnotationPresent(Attribute.class))
            return findByToBy(field.getAnnotation(Attribute.class));
        if (field.isAnnotationPresent(ByClass.class))
            return findByToBy(field.getAnnotation(ByClass.class));
        if (field.isAnnotationPresent(Id.class))
            return findByToBy(field.getAnnotation(Id.class));
        if (field.isAnnotationPresent(ByName.class))
            return findByToBy(field.getAnnotation(ByName.class));
        if (field.isAnnotationPresent(NgRepeat.class))
            return findByToBy(field.getAnnotation(NgRepeat.class));
        if (field.isAnnotationPresent(NgBinding.class))
            return findByToBy(field.getAnnotation(NgBinding.class));
        if (field.isAnnotationPresent(NgModel.class))
            return findByToBy(field.getAnnotation(NgModel.class));
        if (field.isAnnotationPresent(ByTitle.class))
            return findByToBy(field.getAnnotation(ByTitle.class));
        if (field.isAnnotationPresent(Tag.class))
            return findByToBy(field.getAnnotation(Tag.class));
        if (field.isAnnotationPresent(ByType.class))
            return findByToBy(field.getAnnotation(ByType.class));
        if (field.isAnnotationPresent(ByValue.class))
            return findByToBy(field.getAnnotation(ByValue.class));
        return null;
    }
    private static void fillFromAnnotation(BaseElement instance, Field field) {
        try {
            ISetup setup = (ISetup) instance;
            setup.setup(field);
        } catch (Exception ignore) {}
    }

}