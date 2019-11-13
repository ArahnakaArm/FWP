package com.example.deimos.fwp

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class Company(title: String, items: List<Product>) : ExpandableGroup<Product>(title, items)
