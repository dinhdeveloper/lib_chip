package unit.chip.lib_unit_chip.model

import java.security.PrivateKey
import java.security.cert.Certificate


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class EACCredentials(val privateKey: PrivateKey, val chain: Array<Certificate>)