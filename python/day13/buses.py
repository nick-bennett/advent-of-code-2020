__author__ = "Nicholas Bennett"
__copyright__ = "Copyright 2020 Nicholas Bennett"
__license__ = """
Copyright 2020 Nicholas Bennett.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
"""


def main():
    print(find_offset_synch_mod_inverse(
            [37, None, None, None, None, None, None, None, None, None, None,
             None, None, None, None, None, None, None, None, None, None,
             None,
             None, None, None, None, None, 41, None, None, None, None, None,
             None, None, None, None, 457, None, None, None, None, None,
             None,
             None, None, None, None, None, None, 13, 17, None, None, None,
             None,
             None, None, None, None, 23, None, None, None, None, None, 29,
             None,
             431, None, None, None, None, None, None, None, None, None,
             None,
             None, None, None, None, None, None, None, None, 19]))


def mod_inverse(value, modulus):
    prev_r = value
    r = modulus
    prev_s = 1
    s = 0
    while r != 1:
        quotient = prev_r // r
        prev_r, r = r, prev_r - quotient * r
        prev_s, s = s, prev_s - quotient * s
        if r == 0:
            raise ValueError("{0} is not invertible for modulus {1}"
                             .format(value, modulus))
    return s if s > 0 else modulus + s


def find_offset_synch_mod_inverse(route_lengths):
    baseline = 0
    offset = 0
    cycle_length = route_lengths[0]
    for length in route_lengths[1:]:
        offset += 1
        if length:
            additional_offset = baseline % length
            gap = offset + additional_offset
            baseline -= additional_offset
            # inverse = pow(length, -1, cycle_length)
            inverse = mod_inverse(length, cycle_length)
            baseline += inverse * gap % cycle_length * length - offset
            cycle_length *= length
    return baseline


def find_offset_synch_smart_force(route_lengths):
    baseline = 0
    offset = 0
    cycle_length = route_lengths[0]
    for length in route_lengths[1:]:
        offset += 1
        if length:
            while (baseline + offset) % length != 0:
                baseline += cycle_length
            cycle_length *= length
    return baseline


if __name__ == '__main__':
    main()
