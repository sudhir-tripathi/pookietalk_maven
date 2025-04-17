import { useState, useCallback } from 'react';
import { z } from 'zod';
import { validateForm } from '../utils/validation';

interface UseFormProps<T> {
  initialValues: T;
  schema: z.ZodSchema<T>;
  onSubmit: (values: T) => Promise<void>;
}

interface FormState<T> {
  values: T;
  errors: Record<string, string>;
  touched: Record<string, boolean>;
  isSubmitting: boolean;
  submitError: string | null;
}

export function useForm<T extends Record<string, any>>({
  initialValues,
  schema,
  onSubmit,
}: UseFormProps<T>) {
  const [formState, setFormState] = useState<FormState<T>>({
    values: initialValues,
    errors: {},
    touched: {},
    isSubmitting: false,
    submitError: null,
  });

  const setFieldValue = useCallback((field: keyof T, value: any) => {
    setFormState((prev) => ({
      ...prev,
      values: { ...prev.values, [field]: value },
      touched: { ...prev.touched, [field]: true },
    }));
  }, []);

  const setFieldTouched = useCallback((field: keyof T, isTouched = true) => {
    setFormState((prev) => ({
      ...prev,
      touched: { ...prev.touched, [field]: isTouched },
    }));
  }, []);

  const validateField = useCallback((field: keyof T, value: any) => {
    try {
      schema.pick({ [field]: true }).parse({ [field]: value });
      setFormState((prev) => ({
        ...prev,
        errors: { ...prev.errors, [field]: undefined },
      }));
    } catch (error) {
      if (error instanceof z.ZodError) {
        const fieldError = error.errors[0]?.message;
        setFormState((prev) => ({
          ...prev,
          errors: { ...prev.errors, [field]: fieldError },
        }));
      }
    }
  }, [schema]);

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    setFormState((prev) => ({ ...prev, isSubmitting: true, submitError: null }));

    const validation = validateForm(schema, formState.values);
    
    if (!validation.success) {
      const errors: Record<string, string> = {};
      validation.error.errors.forEach((err) => {
        if (err.path[0]) {
          errors[err.path[0] as string] = err.message;
        }
      });
      
      setFormState((prev) => ({
        ...prev,
        errors,
        isSubmitting: false,
      }));
      return;
    }

    try {
      await onSubmit(validation.data);
    } catch (error) {
      setFormState((prev) => ({
        ...prev,
        submitError: error instanceof Error ? error.message : 'An unexpected error occurred',
      }));
    } finally {
      setFormState((prev) => ({ ...prev, isSubmitting: false }));
    }
  }, [schema, formState.values, onSubmit]);

  return {
    values: formState.values,
    errors: formState.errors,
    touched: formState.touched,
    isSubmitting: formState.isSubmitting,
    submitError: formState.submitError,
    setFieldValue,
    setFieldTouched,
    validateField,
    handleSubmit,
  };
}