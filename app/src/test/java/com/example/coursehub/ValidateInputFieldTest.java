package com.example.coursehub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.text.Editable;
import android.widget.EditText;

import com.example.coursehub.service.ValidateInputField;

import org.junit.Test;

public class ValidateInputFieldTest {

    // Returns a trimmed string when given a non-empty EditText object
    @Test
    public void test_returns_trimmed_string() {
        EditText editText = mock(EditText.class);
        // Create a mock Editable object
        Editable editable = mock(Editable.class);

        when(editText.getText()).thenReturn(editable);

        when(editable.toString()).thenReturn("example");
        ValidateInputField validateInputField = new ValidateInputField();
        String result = validateInputField.apply(editText);
        assertEquals("example", result);
    }

    // Returns null when given a null EditText object
    @Test
    public void test_returns_null() {
        ValidateInputField validateInputField = mock(ValidateInputField.class);
        EditText editText = null;
        String result = validateInputField.apply(editText);
        assertNull(result);
    }

}