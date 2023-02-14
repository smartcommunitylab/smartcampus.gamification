import { ArrayInputClasses, ArrayInputContext, ArrayInputProps, composeSyncValidators, FieldTitle, InputHelperText, isRequired, Labeled, LinearProgress, sanitizeInputRestProps, useApplyInputDefaultValues, useFormGroupContext, useFormGroups, useGetValidationErrorMessage } from 'react-admin';
import clsx from 'clsx';
import { useEffect, cloneElement, Children } from 'react';
import { useFieldArray, useFormContext } from 'react-hook-form';

export const CustomArrayInput = (props: ArrayInputProps) => {   
   const {
        className,
        defaultValue,
        label,
        isFetching,
        isLoading,
        children,
        helperText,
        record,
        resource: resourceFromProps,
        source,
        validate,
        variant,
        disabled,
        margin = 'dense',
        ...rest
    } = props;

    const formGroupName = useFormGroupContext();
    const formGroups = useFormGroups();

    const sanitizedValidate = Array.isArray(validate)
        ? composeSyncValidators(validate)
        : validate;
    const getValidationErrorMessage = useGetValidationErrorMessage();

    const {
        getFieldState,
        formState,
        getValues,
        register,
        unregister,
    } = useFormContext();

    const fieldProps = useFieldArray({
        name: source,
        rules: {
            validate: async value => {
                if (!sanitizedValidate) return true;
                const error = await sanitizedValidate(
                    value,
                    getValues(),
                    props
                );

                if (!error) return true;
                return getValidationErrorMessage(error);
            },
        },
    });

    const { isSubmitted } = formState;

    // We need to register the array itself as a field to enable validation at its level
    useEffect(() => {
        register(source);
        formGroups.registerField(source, formGroupName);

        return () => {
            unregister(source, { keepValue: true });
            formGroups.unregisterField(source, formGroupName);
        };
    }, [register, unregister, source, formGroups, formGroupName]);

    useApplyInputDefaultValues(props);

    const { isDirty, error } = getFieldState(source, formState);

    if (isLoading) {
        return (
            <Labeled label={label} className={className}>
                <LinearProgress />
            </Labeled>
        );
    }

    return (
        <Root
            fullWidth
            margin={margin}
            className={clsx(
                'ra-input',
                `ra-input-${source}`,
                ArrayInputClasses.root,
                className
            )}
            error={(isDirty || isSubmitted) && !!error}
            {...sanitizeInputRestProps(rest)}
        >
            <InputLabel
                htmlFor={source}
                className={ArrayInputClasses.label}
                shrink
                error={(isDirty || isSubmitted) && !!error}
            >
                <FieldTitle
                    label={label}
                    source={source}
                    resource={resourceFromProps}
                    isRequired={isRequired(validate)}
                />
            </InputLabel>
            <ArrayInputContext.Provider value={fieldProps}>
                {cloneElement(Children.only(children), {
                    ...fieldProps,
                    record,
                    resource: resourceFromProps,
                    source,
                    variant,
                    margin,
                    disabled,
                })}
            </ArrayInputContext.Provider>
            {!!((isDirty || isSubmitted) && !!error) || helperText ? (
                <FormHelperText error={(isDirty || isSubmitted) && !!error}>
                    <InputHelperText
                        touched={isDirty || isSubmitted}
                        // root property is applicable to built-in validation only,
                        // Resolvers are yet to support useFieldArray root level validation.
                        // Reference: https://react-hook-form.com/api/usefieldarray
                        error={error?.root?.message ?? error?.message}
                        helperText={helperText}
                    />
                </FormHelperText>
            ) : null}
        </Root>
    );
}

import {
    InputLabel,
    FormControl,
    FormHelperText, styled
} from '@mui/material';

const PREFIX = 'RaArrayInput';

const Root = styled(FormControl, {
    name: PREFIX,
    overridesResolver: (props, styles) => styles.root,
})(({ theme }) => ({
    marginTop: 0,
    [`& .${ArrayInputClasses.label}`]: {
        position: 'relative',
        top: theme.spacing(0.5),
        left: theme.spacing(-1.5),
    },
    [`& .${ArrayInputClasses.root}`]: {
        // nested ArrayInput
        paddingLeft: theme.spacing(2),
    },
}));