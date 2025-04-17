import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { useForm } from "../hooks/useForm";
import { loginSchema, type LoginInput } from "../utils/validation";
import authService from "../services/authService";
import { toast } from "react-hot-toast";
import clsx from "clsx";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useAuth();

  const initialValues: LoginInput = {
    email: "",
    password: "",
  };

  const {
    values,
    errors,
    touched,
    isSubmitting,
    submitError,
    setFieldValue,
    setFieldTouched,
    validateField,
    handleSubmit,
  } = useForm<LoginInput>({
    initialValues,
    schema: loginSchema,
    onSubmit: async (values) => {
      try {
        const res = await authService.login(values);
        login(res.token);
        toast.success("Welcome back! 👋");
        navigate("/chat");
      } catch (err: any) {
        toast.error(err?.message || "Login failed. Please try again.");
        throw err;
      }
    },
  });

  const handleBlur = (field: keyof LoginInput) => {
    setFieldTouched(field);
    validateField(field, values[field]);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFieldValue(name as keyof LoginInput, value);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-100 via-white to-primary-100 px-4 py-12">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg animate-fade-in">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Welcome Back 👋
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            Don't have an account yet?{" "}
            <Link
              to="/register"
              className="font-medium text-primary-600 hover:text-primary-500 transition-colors"
            >
              Sign up
            </Link>
          </p>
        </div>

        {submitError && (
          <div className="rounded-md bg-red-50 p-4 animate-slide-in">
            <div className="flex">
              <div className="flex-shrink-0">
                <svg
                  className="h-5 w-5 text-red-400"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                    clipRule="evenodd"
                  />
                </svg>
              </div>
              <div className="ml-3">
                <p className="text-sm text-red-700">{submitError}</p>
              </div>
            </div>
          </div>
        )}

        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-4 rounded-md">
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700"
              >
                Email address
              </label>
              <div className="mt-1">
                <input
                  id="email"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  value={values.email}
                  onChange={handleChange}
                  onBlur={() => handleBlur("email")}
                  className={clsx(
                    "appearance-none block w-full px-3 py-2 border rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm transition-colors",
                    {
                      "border-red-300": touched.email && errors.email,
                      "border-gray-300": !(touched.email && errors.email),
                    }
                  )}
                  placeholder="you@example.com"
                />
                {touched.email && errors.email && (
                  <p className="mt-2 text-sm text-red-600 animate-slide-in">
                    {errors.email}
                  </p>
                )}
              </div>
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700"
              >
                Password
              </label>
              <div className="mt-1">
                <input
                  id="password"
                  name="password"
                  type="password"
                  autoComplete="current-password"
                  required
                  value={values.password}
                  onChange={handleChange}
                  onBlur={() => handleBlur("password")}
                  className={clsx(
                    "appearance-none block w-full px-3 py-2 border rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm transition-colors",
                    {
                      "border-red-300": touched.password && errors.password,
                      "border-gray-300": !(touched.password && errors.password),
                    }
                  )}
                  placeholder="••••••••"
                />
                {touched.password && errors.password && (
                  <p className="mt-2 text-sm text-red-600 animate-slide-in">
                    {errors.password}
                  </p>
                )}
              </div>
            </div>
          </div>

          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <input
                id="remember-me"
                name="remember-me"
                type="checkbox"
                className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
              />
              <label
                htmlFor="remember-me"
                className="ml-2 block text-sm text-gray-900"
              >
                Remember me
              </label>
            </div>

            <div className="text-sm">
              <Link
                to="/forgot-password"
                className="font-medium text-primary-600 hover:text-primary-500 transition-colors"
              >
                Forgot your password?
              </Link>
            </div>
          </div>

          <div>
            <button
              type="submit"
              disabled={isSubmitting}
              className={clsx(
                "group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors",
                {
                  "bg-primary-600 hover:bg-primary-700": !isSubmitting,
                  "bg-primary-400 cursor-not-allowed": isSubmitting,
                }
              )}
            >
              {isSubmitting ? (
                <>
                  <svg
                    className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                  >
                    <circle
                      className="opacity-25"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      strokeWidth="4"
                    ></circle>
                    <path
                      className="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                    ></path>
                  </svg>
                  Signing in...
                </>
              ) : (
                "Sign in"
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;