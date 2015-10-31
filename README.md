# cash-register
Simple experimental cash register application.

REST URLs follow the format: controller/<controller name>/<optional: descriptor>/<action>
For example: controller/order/find

The controller layer is currently a pretty simple pass through, as there aren't requirements for conversion of the
data model, since it is already well-aligned with what is needed for the view.