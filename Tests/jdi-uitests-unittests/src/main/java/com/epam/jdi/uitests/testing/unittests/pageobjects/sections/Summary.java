package com.epam.jdi.uitests.testing.unittests.pageobjects.sections;

import com.epam.jdi.uitests.core.interfaces.common.IButton;
import com.epam.jdi.uitests.testing.unittests.enums.Even;
import com.epam.jdi.uitests.testing.unittests.enums.Odds;
import com.epam.jdi.uitests.web.selenium.elements.complex.RadioButtons;
import com.epam.jdi.uitests.web.selenium.elements.complex.Selector;
import com.epam.jdi.uitests.web.selenium.elements.composite.Section;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by Roman_Iovlev on 9/15/2015.
 */
public class Summary extends Section {
    @FindBy(css = "#odds-selector p")
    public Selector<Odds> odds;
    @FindBy(css = "#odds-selector p")
    public Selector<Odds> oddsWithSelected = new Selector<Odds>() {
        @Override
        public boolean isSelected(WebElement el) {
            return el.findElement(By.tagName("input")).getAttribute("checked") != null;
        }
    };
    @FindBy(css = "#odds-selector p")
    public RadioButtons<Odds> oddsR;
    @FindBy(css = "#odds-selector p")
    public RadioButtons<Odds> oddsRWithSelected = new RadioButtons<Odds>() {
        @Override
        public boolean isSelected(WebElement el) {
            return el.findElement(By.tagName("input")).getAttribute("checked") != null;
        }
    };
    @FindBy(css = "#even-selector p")
    public Selector<Even> even;
    @FindBy(id = "calculate-button")
    public IButton calculate;

}
