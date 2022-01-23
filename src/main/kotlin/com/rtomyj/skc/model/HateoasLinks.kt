package com.rtomyj.skc.model

interface HateoasLinks {
    fun setSelfLink()
    fun setLinks()

    companion object {
        @JvmStatic
        fun setLinks(instances: List<HateoasLinks>) {
            instances
                .forEach { it.setLinks() }
        }
    }
}