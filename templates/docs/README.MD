# {{ project_name }}

This is a {% if program_type == "spark" %}Spark{% endif %} {{ version }} project in {{ language }} {% if compiler is not none %}compiled with {{ compiler }}{% endif %}{% if components is not none and components|length > 0 %} with : {% for f in components %}{{ f }}, {% endfor %}{% endif %}{% if kerberos is sameas true %} and Kerberos enabled{% endif %}.


TODO : Add description here



