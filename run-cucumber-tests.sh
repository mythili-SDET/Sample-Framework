#!/bin/bash

# Cucumber Test Runner Script
# This script provides various options to run Cucumber BDD tests

echo "=== Cucumber BDD Test Runner ==="
echo ""

# Function to display usage
show_usage() {
    echo "Usage: $0 [OPTION]"
    echo ""
    echo "Options:"
    echo "  all          Run all Cucumber tests"
    echo "  ui           Run only UI tests"
    echo "  api          Run only API tests"
    echo "  database     Run only database tests"
    echo "  smoke        Run only smoke tests"
    echo "  regression   Run only regression tests"
    echo "  parallel     Run tests in parallel"
    echo "  help         Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 all"
    echo "  $0 ui"
    echo "  $0 smoke"
    echo "  $0 parallel"
}

# Function to run tests
run_tests() {
    local test_type=$1
    local parallel=$2
    
    echo "Running $test_type tests..."
    echo ""
    
    case $test_type in
        "all")
            if [ "$parallel" = "true" ]; then
                mvn test -Dtest=CucumberTestRunner -Dparallel=classes -DthreadCount=3
            else
                mvn test -Dtest=CucumberTestRunner
            fi
            ;;
        "ui")
            if [ "$parallel" = "true" ]; then
                mvn test -Dtest=UITestRunner -Dparallel=classes -DthreadCount=2
            else
                mvn test -Dtest=UITestRunner
            fi
            ;;
        "api")
            if [ "$parallel" = "true" ]; then
                mvn test -Dtest=APITestRunner -Dparallel=classes -DthreadCount=2
            else
                mvn test -Dtest=APITestRunner
            fi
            ;;
        "database")
            if [ "$parallel" = "true" ]; then
                mvn test -Dtest=DatabaseTestRunner -Dparallel=classes -DthreadCount=2
            else
                mvn test -Dtest=DatabaseTestRunner
            fi
            ;;
        "smoke")
            if [ "$parallel" = "true" ]; then
                mvn test -Dtest=SmokeTestRunner -Dparallel=classes -DthreadCount=2
            else
                mvn test -Dtest=SmokeTestRunner
            fi
            ;;
        "regression")
            if [ "$parallel" = "true" ]; then
                mvn test -Dtest=RegressionTestRunner -Dparallel=classes -DthreadCount=3
            else
                mvn test -Dtest=RegressionTestRunner
            fi
            ;;
        *)
            echo "Unknown test type: $test_type"
            show_usage
            exit 1
            ;;
    esac
}

# Function to run tests by tags
run_tests_by_tags() {
    local tags=$1
    echo "Running tests with tags: $tags"
    echo ""
    mvn test -Dcucumber.filter.tags="$tags"
}

# Main script logic
case "$1" in
    "all")
        run_tests "all" "false"
        ;;
    "ui")
        run_tests "ui" "false"
        ;;
    "api")
        run_tests "api" "false"
        ;;
    "database")
        run_tests "database" "false"
        ;;
    "smoke")
        run_tests "smoke" "false"
        ;;
    "regression")
        run_tests "regression" "false"
        ;;
    "parallel")
        echo "Running all tests in parallel..."
        echo ""
        run_tests "all" "true"
        ;;
    "tags")
        if [ -z "$2" ]; then
            echo "Error: Please specify tags"
            echo "Example: $0 tags @smoke"
            exit 1
        fi
        run_tests_by_tags "$2"
        ;;
    "help"|"-h"|"--help")
        show_usage
        ;;
    "")
        echo "Error: Please specify an option"
        echo ""
        show_usage
        exit 1
        ;;
    *)
        echo "Error: Unknown option '$1'"
        echo ""
        show_usage
        exit 1
        ;;
esac

echo ""
echo "=== Test execution completed ==="
echo "Check the following directories for results:"
echo "- target/cucumber-reports/ (Cucumber reports)"
echo "- target/surefire-reports/ (TestNG reports)"
echo "- reports/ (Extent reports)"
echo "- logs/ (Framework logs)"