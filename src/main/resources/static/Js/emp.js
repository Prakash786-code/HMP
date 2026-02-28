document.addEventListener("DOMContentLoaded", function () {

    const deptSelect = document.getElementById("department");
    const desigSelect = document.getElementById("designation");

    if (!deptSelect || !desigSelect) return;

    const designationMap = {
        "Development": [
            "Software Engineer",
            "Senior Developer",
            "Team Lead",
            "Project Manager"
        ],
        "QA & Automation Testing": [
            "QA Engineer",
            "Automation Engineer",
            "Test Lead",
            "QA Manager"
        ],
        "Networking": [
            "Network Engineer",
            "System Administrator",
            "Network Analyst",
            "Infrastructure Manager"
        ],
        "HR Team": [
            "HR Executive",
            "Recruiter",
            "HR Manager",
            "Talent Acquisition Lead"
        ],
        "Security": [
            "Security Officer",
            "Security Analyst",
            "Cyber Security Engineer",
            "Security Manager"
        ],
        "Sales Marketing": [
            "Sales Executive",
            "Marketing Executive",
            "Business Development Manager",
            "Sales Manager"
        ]
    };

    deptSelect.addEventListener("change", function () {

        const selectedDept = this.value;

        desigSelect.innerHTML = '<option value="">Select Designation</option>';

        if (designationMap[selectedDept]) {

            designationMap[selectedDept].forEach(function (designation) {

                const option = document.createElement("option");
                option.value = designation;
                option.textContent = designation;

                desigSelect.appendChild(option);
            });
        }
    });

});